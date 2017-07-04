package com.github.hualuomoli.sample.gateway.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hualuomoli.gateway.api.anno.ApiMethod;
import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.CodeEnum;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.api.support.security.AES;
import com.github.hualuomoli.gateway.api.support.security.RSA;
import com.github.hualuomoli.gateway.client.GatewayGenericClient;
import com.github.hualuomoli.gateway.client.Parser;
import com.github.hualuomoli.gateway.client.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.client.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;
import com.github.hualuomoli.gateway.client.interceptor.encrypt.EncryptionInterceptor;
import com.github.hualuomoli.gateway.client.interceptor.encrypt.SignatureInterceptor;
import com.github.hualuomoli.gateway.client.invoker.ClientInvoker;
import com.github.hualuomoli.gateway.client.lang.ClientException;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.github.hualuomoli.tool.http.HttpClient;
import com.github.hualuomoli.tool.http.HttpDefaultClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ClientControllerTest {

  protected static final Logger logger = LoggerFactory.getLogger(ClientControllerTest.class);

  private static final String serverURL = "http://localhost/gateway";
  protected GatewayGenericClient client;
  protected HttpClient httpClient;

  @Before
  public void before() {

    String partnerId = "tester";

    httpClient = new HttpDefaultClient(serverURL);

    GatewayGenericClient genericClient = new GatewayGenericClient();
    genericClient.setVersion("1.0.0");
    genericClient.setPartnerId(partnerId);
    genericClient.setSignature(SignatureEnum.RSA);
    genericClient.setEncryption(EncryptionEnum.AES);
    genericClient.setInterceptors(interceptors());
    genericClient.setInvoker(new ClientInvoker() {

      @Override
      public String call(Request request) throws BusinessException, ClientException {
        try {
          return httpClient.urlencoded(request);
        } catch (IOException e) {
          throw new ClientException(CodeEnum.ERROR, e);
        }
        // end
      }
    });
    genericClient.setJsonParser(new JSONParser() {

      @Override
      public String toJsonString(Object object) {
        if (object == null) {
          return null;
        }
        if (String.class.isAssignableFrom(object.getClass())) {
          return (String) object;
        }
        return JSON.toJSONString(object);
      }

      @Override
      public <T> T parseObject(String content, Class<T> clazz) {
        if (content == null) {
          return null;
        }
        return JSON.parseObject(content, clazz);
      }

      @Override
      public <T> List<T> parseArray(String content, Class<T> clazz) {
        if (content == null) {
          return null;
        }
        return JSON.parseArray(content, clazz);
      }

      @Override
      public Map<String, Object> parse(String content) {
        if (content == null) {
          return null;
        }
        Map<String, Object> map = Maps.newHashMap();
        JSONObject json = JSON.parseObject(content);
        Set<String> keys = json.keySet();
        for (String key : keys) {
          map.put(key, json.get(key));
        }
        return map;
      }
    });
    genericClient.setEncryptionDealers(Lists.newArrayList(encryptionDealer()));
    genericClient.setSignatureDealers(Lists.newArrayList(signatureDealer()));
    genericClient.setParser(new Parser() {

      @Override
      public String method(Object object) {
        return object.getClass().getAnnotation(ApiMethod.class).value();
      }

      @Override
      public PageName pageName() {
        return new PageName() {

          @Override
          public String pageSize() {
            return "pageSize";
          }

          @Override
          public String pageNumber() {
            return "pageNumber";
          }

          @Override
          public String datas() {
            return "dataList";
          }

          @Override
          public String count() {
            return "count";
          }
        };
      }
    });
    client = genericClient;
  }

  private static List<Interceptor> interceptors() {
    List<Interceptor> interceptors = Lists.newArrayList();

    // pre = 其他 - 签名 - 加密
    // post = 解密 - 验签 - 其他
    interceptors.add(new Interceptor() {
      final Logger logger = LoggerFactory.getLogger(Interceptor.class);

      @Override
      public void preHandle(Request request) {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(Request request, Response response) throws InvalidDataException {
        logger.debug("响应业务内容={}", response.getResult());
      }
    });
    interceptors.add(new SignatureInterceptor());
    interceptors.add(new EncryptionInterceptor());

    return interceptors;
  }

  private static EncryptionDealer encryptionDealer() {
    return new EncryptionDealer() {

      @Override
      public boolean support(EncryptionEnum encryption) {
        return encryption == EncryptionEnum.AES;
      }

      @Override
      public String encrypt(String data, String partnerId) {
        return AES.encrypt(Key.SALT, data);
      }

      @Override
      public String decrypt(String data, String partnerId) throws InvalidDataException {
        return AES.decrypt(Key.SALT, data);
      }
    };
  }

  private static SignatureDealer signatureDealer() {
    return new SignatureDealer() {

      @Override
      public boolean support(SignatureEnum signature) {
        return signature == SignatureEnum.RSA;
      }

      @Override
      public String sign(String origin, String partnerId) {
        return RSA.sign(Key.CLIENT_PRIVATE_KEY, origin);
      }

      @Override
      public boolean verify(String origin, String sign, String partnerId) throws InvalidDataException {
        return RSA.verify(Key.SERVER_PUBLIC_KEY, origin, sign);
      }

    };
  }

}
