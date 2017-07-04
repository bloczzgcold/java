package com.github.hualuomoli.sample.gateway.server.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.hualuomoli.gateway.api.anno.ApiMethod;
import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
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
import com.github.hualuomoli.gateway.client.interceptor.sign.SignatureInterceptor;
import com.github.hualuomoli.gateway.client.invoker.ClientInvoker;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.github.hualuomoli.tool.http.Header;
import com.github.hualuomoli.tool.http.HttpClient;
import com.github.hualuomoli.tool.http.HttpDefaultClient;
import com.github.hualuomoli.tool.http.parser.DefaultParser;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ClientControllerTest {

  protected static final Logger logger = LoggerFactory.getLogger(ClientControllerTest.class);

  private static final String serverURL = "http://localhost/gateway";

  private static com.github.hualuomoli.tool.http.parser.Parser httpParser = new DefaultParser();
  private static HttpClient httpClient;

  protected GatewayGenericClient client;
  protected ClientInvoker invoker;

  @BeforeClass
  public static void beforeClass() {
    httpClient = new HttpDefaultClient(serverURL);
  }

  @Before
  public void before() {

    String partnerId = "tester";

    client = new GatewayGenericClient("1.0.0", partnerId);
    client.setJsonParser(new JSONParser() {

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
    client.setParser(new Parser() {

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
    client.setInvoker(invoker = new ClientInvoker() {

      private String result;
      private List<Header> requestHeaders = Lists.newArrayList();
      private List<Header> responseHeaders = Lists.newArrayList();

      @Override
      public String getResult() {
        return result;
      }

      @Override
      public Set<String> getReponseHeaders() {
        Set<String> names = Sets.newHashSet();
        for (Header header : responseHeaders) {
          names.add(header.getName());
        }
        return names;
      }

      @Override
      public String[] getReponseHeader(String name) {
        if (StringUtils.isBlank(name)) {
          return null;
        }
        for (Header header : responseHeaders) {
          if (StringUtils.equals(name, header.getName())) {
            return header.getValue();
          }
        }
        return null;
      }

      @Override
      public ClientInvoker call(Request request) throws IOException {
        this.result = httpClient.urlencoded(httpParser.parse(request, null), requestHeaders, responseHeaders);
        return this;
      }

      @Override
      public ClientInvoker addRequestHeader(String name, String value) {
        requestHeaders.add(new Header(name, new String[] { value }));
        return this;
      }
    });

    // add 先签名再加密
    client.addInterceptor(this.logIntercetpro());
    client.addSignatureInterceptor(new SignatureInterceptor(Lists.newArrayList(this.signatureDealer())));
    client.addEncryptionInterceptor(new EncryptionInterceptor(Lists.newArrayList(this.encryptionDealer())));

    // default use RSA sign, AES encrypt
    client.addSignature(SignatureEnum.RSA);
    client.addEncryption(EncryptionEnum.AES);
  }

  private Interceptor logIntercetpro() {
    return new Interceptor() {
      final Logger logger = LoggerFactory.getLogger(Interceptor.class);

      @Override
      public void preHandle(Request request) {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(Request request, Response response) throws InvalidDataException {
        logger.debug("响应业务内容={}", response.getResult());
      }
    };
  }

  private EncryptionDealer encryptionDealer() {
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

  private SignatureDealer signatureDealer() {
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
