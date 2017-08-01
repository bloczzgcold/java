package com.github.hualuomoli.sample.gateway.server.biz.type.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.gateway.android.GatewayClient;
import com.github.hualuomoli.gateway.android.base64.Base64;
import com.github.hualuomoli.gateway.android.callback.CallbackArray;
import com.github.hualuomoli.gateway.android.callback.CallbackObject;
import com.github.hualuomoli.gateway.android.entity.Request;
import com.github.hualuomoli.gateway.android.entity.Response;
import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;
import com.github.hualuomoli.gateway.android.http.HttpInvoker;
import com.github.hualuomoli.gateway.android.json.JSONParser;
import com.github.hualuomoli.gateway.android.lang.GatewayException;
import com.github.hualuomoli.gateway.android.security.Security;
import com.github.hualuomoli.gateway.android.support.AES;
import com.github.hualuomoli.gateway.android.support.RSA;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.InArrayRequest;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.InArrayResponse;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.InObjectRequest;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.InObjectResponse;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.OutArrayRequest;
import com.github.hualuomoli.sample.gateway.server.biz.type.entity.OutObjectRequest;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.github.hualuomoli.tool.http.HttpDefaultClient;
import com.github.hualuomoli.tool.http.parser.DefaultParser;
import com.google.common.collect.Lists;

import android.util.Log;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TypeControllerObjectClientForAndroidTest {

  private static final String TAG = "TypeControllerObjectClientForAndroidTest";

  private static final String URL = "http://localhost/gateway";
  private static final String partnerId = "tester";
  private static final Charset CHARSET = Charset.forName("UTF-8");
  private static final Base64 BASE64 = new MyBase64();
  private static final JSONParser JSON_PARSER = new MyJSONParser();
  private static final Security SECURITY = new MySecurity();

  // 请求object
  @Test
  public void test01RequestObject() {
    InObjectRequest req = new InObjectRequest();
    req.setUsername("hualuomoli");
    req.setNickname("花落寞离");
    req.setAge(18);

    new GatewayClient(URL) //
        .setBase64(BASE64)//
        .setCharset(CHARSET)//
        .setJsonParser(JSON_PARSER)//
        .setPartnerId(partnerId)//
        .setSecurity(SECURITY)//
        .setHttpInvoker(new MyHttpInvoker())

        .execute("type.inObject", JSON.toJSONString(req), new MyCallbackObject<InObjectResponse>() {
        });

  }

  // 请求list
  @Test
  public void test02RequestArray() {
    List<InArrayRequest> list = Lists.newArrayList();

    InArrayRequest req1 = new InArrayRequest();
    req1.setUsername("hualuomoli");
    req1.setNickname("花落寞离");
    req1.setAge(18);

    InArrayRequest req2 = new InArrayRequest();
    req2.setUsername("tester");
    req2.setNickname("测试");
    req2.setAge(20);

    list.add(req1);
    list.add(req2);

    new GatewayClient(URL) //
        .setBase64(BASE64)//
        .setCharset(CHARSET)//
        .setJsonParser(JSON_PARSER)//
        .setPartnerId(partnerId)//
        .setSecurity(SECURITY)//
        .setHttpInvoker(new MyHttpInvoker())

        .execute("type.inArray", JSON.toJSONString(list), new MyCallbackObject<InArrayResponse>() {
        });

  }

  // 返回object
  @Test
  public void test03ResponseObject() {
    OutObjectRequest req = new OutObjectRequest();

    new GatewayClient(URL) //
        .setBase64(BASE64)//
        .setCharset(CHARSET)//
        .setJsonParser(JSON_PARSER)//
        .setPartnerId(partnerId)//
        .setSecurity(SECURITY)//
        .setHttpInvoker(new MyHttpInvoker())

        .execute("type.outObject", JSON.toJSONString(req), new MyCallbackObject<InObjectResponse>() {
        });
  }

  // 返回list
  @Test
  public void test04ResponseArray() {
    OutArrayRequest req = new OutArrayRequest();
    new GatewayClient(URL) //
        .setBase64(BASE64)//
        .setCharset(CHARSET)//
        .setJsonParser(JSON_PARSER)//
        .setPartnerId(partnerId)//
        .setSecurity(SECURITY)//
        .setHttpInvoker(new MyHttpInvoker())

        .execute("type.outArray", JSON.toJSONString(req), new MyCallbackArray<InObjectResponse>() {
        });

  }

  //  // 返回page
  //  @Test
  //  public void test05ResponsePage() {
  //    OutPageRequest req = new OutPageRequest();
  //
  //    Page<OutPageResponse> page = client.callPage(req, OutPageResponse.class);
  //    Assert.assertEquals(1, page.getPageNumber().intValue());
  //    Assert.assertEquals(10, page.getPageSize().intValue());
  //    Assert.assertEquals(3, page.getCount().intValue());
  //
  //    List<OutPageResponse> list = page.getDataList();
  //    Assert.assertEquals(3, list.size());
  //
  //    Assert.assertEquals("hualuomoli", list.get(0).getUsername());
  //    Assert.assertEquals("花落寞离", list.get(0).getNickname());
  //    Assert.assertEquals(18, list.get(0).getAge().intValue());
  //
  //    Assert.assertEquals("tester", list.get(1).getUsername());
  //    Assert.assertEquals("测试", list.get(1).getNickname());
  //    Assert.assertEquals(20, list.get(1).getAge().intValue());
  //
  //    Assert.assertEquals("jack", list.get(2).getUsername());
  //    Assert.assertEquals("杰克", list.get(2).getNickname());
  //    Assert.assertEquals(26, list.get(2).getAge().intValue());
  //  }

  private static class MyBase64 implements Base64 {

    @Override
    public String encode(byte[] bytes) {
      return com.github.hualuomoli.gateway.android.support.Base64.encode(bytes);
    }

    @Override
    public byte[] decode(String data) {
      return com.github.hualuomoli.gateway.android.support.Base64.decode(data);
    }

  }

  private static class MyJSONParser implements JSONParser {

    @Override
    public <T> T parseObject(String json, Class<T> clazz) {
      return JSON.parseObject(json, clazz);
    }

    @Override
    public <T> List<T> parseArray(String json, Class<T> clazz) {
      return JSON.parseArray(json, clazz);
    }

  }

  private static class MySecurity implements Security {

    @Override
    public String getSignType() {
      return "RSA";
    }

    @Override
    public byte[] sign(byte[] origin) throws GatewayException {
      return RSA.sign(BASE64.decode(Key.CLIENT_PRIVATE_KEY), origin);
    }

    @Override
    public boolean verify(byte[] origin, byte[] sign) throws GatewayException {
      return RSA.verify(BASE64.decode(Key.SERVER_PUBLIC_KEY), origin, sign);
    }

    @Override
    public String getEncryptType() {
      return "AES";
    }

    @Override
    public byte[] encrypt(byte[] text) throws GatewayException {
      return AES.encrypt(BASE64.decode(Key.SALT), text);
    }

    @Override
    public byte[] decrypt(byte[] chipherText) throws GatewayException {
      return AES.decrypt(BASE64.decode(Key.SALT), chipherText);
    }

  }

  private static class MyHttpInvoker implements HttpInvoker {

    @Override
    public Response execute(String url, Request request, JSONParser jsonParser) throws GatewayException {
      try {
        String result = new HttpDefaultClient(url).urlencoded(new DefaultParser().parse(request));
        return jsonParser.parseObject(result, Response.class);
      } catch (IOException e) {
        throw new GatewayException(ErrorTypeEnum.HTTP_SERVER_RUNTIME_ERROR, e.getMessage());
      } catch (Exception e) {
        throw new GatewayException(ErrorTypeEnum.UNKNOWN, e.getMessage());
      }
      // end
    }

  }

  private static class MyCallbackObject<T> implements CallbackObject<T> {

    @Override
    public void onError(ErrorTypeEnum errorType, String message) {
      Log.e(TAG, "" + message + "[" + errorType.name() + "]");
    }

    @Override
    public void onBusinessError(String subCode, String subMessage, String subErrorCode) {
      Log.e(TAG, "" + subMessage + "[" + subErrorCode + "]" + "(" + subCode + ")");
    }

    @Override
    public void onSuccess(T result) {
      Log.i(TAG, result.toString());
    }

  }

  private static class MyCallbackArray<T> implements CallbackArray<T> {

    @Override
    public void onError(ErrorTypeEnum errorType, String message) {
      Log.e(TAG, "" + message + "[" + errorType.name() + "]");
    }

    @Override
    public void onBusinessError(String subCode, String subMessage, String subErrorCode) {
      Log.e(TAG, "" + subMessage + "[" + subErrorCode + "]" + "(" + subCode + ")");
    }

    @Override
    public void onSuccess(List<T> results) {
      Log.i(TAG, results.toString());
    }

  }

}
