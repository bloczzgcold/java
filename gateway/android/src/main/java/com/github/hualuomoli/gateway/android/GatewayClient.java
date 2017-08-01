package com.github.hualuomoli.gateway.android;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.github.hualuomoli.gateway.android.base64.Base64;
import com.github.hualuomoli.gateway.android.callback.Callback;
import com.github.hualuomoli.gateway.android.entity.Request;
import com.github.hualuomoli.gateway.android.entity.Response;
import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;
import com.github.hualuomoli.gateway.android.http.HttpInvoker;
import com.github.hualuomoli.gateway.android.json.JSONParser;
import com.github.hualuomoli.gateway.android.lang.GatewayException;
import com.github.hualuomoli.gateway.android.security.Security;

import android.util.Log;

/**
 * 网关客户端
 */
public class GatewayClient {

  private static final String GATEWAY_VERSION = "1.0";
  private static final String TAG = "GatewayClient";

  // 数据编码集合
  private Charset charset;
  // 服务器地址
  private String url;
  // 合作伙伴ID
  private String partnerId;
  // HTTP执行器
  private HttpInvoker httpInvoker;
  // 安全
  private Security security;
  // Base64处理
  private Base64 base64;
  // JSON转换器
  private JSONParser jsonParser;

  public GatewayClient(String url) {
    super();
    this.url = url;
  }

  public GatewayClient setCharset(Charset charset) {
    this.charset = charset;
    return this;
  }

  public GatewayClient setPartnerId(String partnerId) {
    this.partnerId = partnerId;
    return this;
  }

  public GatewayClient setHttpInvoker(HttpInvoker httpInvoker) {
    this.httpInvoker = httpInvoker;
    return this;
  }

  public GatewayClient setSecurity(Security security) {
    this.security = security;
    return this;
  }

  public GatewayClient setBase64(Base64 base64) {
    this.base64 = base64;
    return this;
  }

  public GatewayClient setJsonParser(JSONParser jsonParser) {
    this.jsonParser = jsonParser;
    return this;
  }

  public <T> void execute(String method, String bizContent, Callback<T> callback) {
    // 没有定义回调
    if (callback == null) {
      Log.w(TAG, "请设置回调函数");
      return;
    }

    Request req = new Request();
    req.setVersion(GATEWAY_VERSION);
    req.setPartnerId(this.partnerId);
    req.setMethod(method);
    req.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    req.setNonceStr(UUID.randomUUID().toString().replaceAll("[-]", "").substring(10, 26)); // 32
    req.setBizContent(bizContent);
    req.setSignType(this.security.getSignType());
    req.setEncryptType(this.security.getEncryptType());

    // 先签名
    String reqSignOrigin = this.getSignOrigin(req, "sign");
    req.setSign(this.base64.encode(this.security.sign(reqSignOrigin.getBytes(this.charset))));

    // 如果需要加密,执行加密 
    if (req.getEncryptType() != null) {
      req.setBizContent(this.base64.encode(this.security.encrypt(req.getBizContent().getBytes(this.charset))));
    }

    try {
      // 执行HTTP请求
      Response res = httpInvoker.execute(this.url, req, this.jsonParser);

      // 解密
      if (req.getEncryptType() != null) {
        res.setResult(new String(this.security.decrypt(this.base64.decode(res.getResult())), this.charset));
      }

      // 验证签名
      String resSignOrigin = this.getSignOrigin(res, "sign");
      if (!this.security.verify(resSignOrigin.getBytes(this.charset), this.base64.decode(res.getSign()))) {
        throw new GatewayException(ErrorTypeEnum.SECURITY_VERIFY_ERROR, "认证服务器签名失败,签名不合法");
      }

      // 判断是否为成功
      if ("SUCCESS".equalsIgnoreCase(res.getCode())) {
        this.success(res.getResult(), callback);
      } else if ("BUSINESS".equalsIgnoreCase(res.getCode())) {
        callback.onBusinessError(res.getSubCode(), res.getSubMessage(), res.getSubErrorCode());
      } else {
        // 其他不可知错误
        throw new Exception(res.getMessage() + "[" + res.getCode() + "]");
      }
    } catch (GatewayException e) {
      // 网关处理事变
      callback.onError(e.getErrorType(), e.getMessage());
      return;
    } catch (Exception e) {
      Log.e(TAG, e.getMessage());
      callback.onError(ErrorTypeEnum.UNKNOWN, e.getMessage());
      return;
    }

  }

  /**
   * 成功处理
   * @param result 数据
   * @param callback 回调
   * @throws GatewayException 网关错误{@link ErrorTypeEnum#INVALID_SERVER_JSON}
   */
  private <T> void success(String result, Callback<T> callback) throws GatewayException {
    try {
      ParameterizedType parameterized = (ParameterizedType) callback.getClass().getGenericSuperclass();
      Type type = parameterized.getActualTypeArguments()[0];
      T object = this.jsonParser.parseObject(result, type);
      callback.onSuccess(object);
    } catch (Exception e) {
      Log.w(TAG, e.getMessage());
    }
    // end
  }

  /**
   * 获取签名原文
   * @param obj 数据
   * @param ignores 忽略
   * @return 签名原文
   */
  private String getSignOrigin(Object obj, String... ignores) {
    Set<String> set = new HashSet<String>();
    if (ignores != null && ignores.length > 0) {
      for (String ignore : ignores) {
        set.add(ignore);
      }
    }
    return this.getSignOrigin(obj, set);
  }

  /**
   * 获取签名原文
   * @param obj 数据
   * @param ignores 忽略
   * @return 签名原文
   */
  private String getSignOrigin(Object obj, Set<String> ignores) {
    StringBuilder buffer = new StringBuilder();

    Class<?> clazz = obj.getClass();
    Field[] fields = clazz.getDeclaredFields();
    List<Param> params = new ArrayList<Param>();

    for (Field field : fields) {
      String name = field.getName();
      if (ignores.contains(name)) {
        continue;
      }
      Object value = null;
      // getter method
      String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
      try {
        value = clazz.getMethod(methodName).invoke(obj);
      } catch (Exception e) {
        Log.e(TAG, e.getMessage());
      }

      if (value == null) {
        continue;
      }

      // add
      params.add(new Param(name, value.toString()));
    }

    // sort by ASICC
    Collections.sort(params, new Comparator<Param>() {

      @Override
      public int compare(Param o1, Param o2) {
        return o1.name.compareTo(o2.name);
      }
    });

    for (int i = 0, size = params.size(); i < size; i++) {
      Param param = params.get(i);
      buffer.append("&").append(param.name).append("=").append(param.value);
    }

    return buffer.toString().substring(1);
  }

  // 参数
  private class Param {
    String name;
    String value;

    Param(String name, String value) {
      this.name = name;
      this.value = value;
    }
  }

}
