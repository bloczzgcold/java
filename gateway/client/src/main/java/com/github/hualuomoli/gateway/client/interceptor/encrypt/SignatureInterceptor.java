package com.github.hualuomoli.gateway.client.interceptor.encrypt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.client.DealerUtils;
import com.github.hualuomoli.gateway.client.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;

/**
 * 签名/验签拦截器
 */
public class SignatureInterceptor implements Interceptor {

  private static final Logger logger = Logger.getLogger(SignatureInterceptor.class.getName());

  @Override
  public void preHandle(Request request) {

    // 获取类型
    SignatureEnum signature = this.getType(request);
    // 获取处理类
    SignatureDealer dealer = DealerUtils.getSignatureDealer(signature);

    // 获取签名原文
    StringBuilder buffer = new StringBuilder();
    List<Data> datas = this.getDatas(request);
    for (Data data : datas) {
      buffer.append("&").append(data.name).append("=").append(data.value);
    }
    String origin = buffer.toString().substring(1);

    if (logger.isLoggable(Level.INFO)) {
      logger.info("请求签名原文=" + origin);
    }

    // 设置签名
    String sign = dealer.sign(origin, request.getPartnerId());
    request.setSign(sign);
  }

  @Override
  public void postHandle(Request request, Response response) throws InvalidDataException {
    // 获取类型
    SignatureEnum signature = this.getType(request);
    // 获取处理类
    SignatureDealer dealer = DealerUtils.getSignatureDealer(signature);

    // 获取签名原文
    StringBuilder buffer = new StringBuilder();
    List<Data> datas = this.getDatas(response);
    for (Data data : datas) {
      buffer.append("&").append(data.name).append("=").append(data.value);
    }
    String origin = buffer.toString().substring(1);

    if (logger.isLoggable(Level.INFO)) {
      logger.info("响应签名原文=" + origin);
    }

    // 验证签名
    boolean success = dealer.verify(origin, response.getSign(), request.getPartnerId());
    if (!success) {
      throw new InvalidDataException("invalid sign data.");
    }
  }

  private List<Data> getDatas(Object object) {
    Class<?> clazz = object.getClass();

    Field[] fields = clazz.getDeclaredFields();
    List<Data> datas = new ArrayList<Data>();
    for (Field field : fields) {
      String name = field.getName();
      if (name.equals("sign")) {
        continue;
      }
      String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
      try {
        Object value = clazz.getMethod(methodName).invoke(object);
        if (value == null) {
          continue;
        }
        datas.add(new Data(name, value.toString()));
      } catch (Exception e) {
        if (logger.isLoggable(Level.WARNING)) {
          logger.warning(e.getMessage());
        }
      }
    }

    Collections.sort(datas, new Comparator<Data>() {

      @Override
      public int compare(Data o1, Data o2) {
        return o1.name.compareTo(o2.name);
      }
    });

    return datas;
  }

  private class Data {
    String name;
    String value;

    public Data(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }
  }

  /**
   * 获取类型
   * @param request 网关请求
   * @return 类型
   */
  private SignatureEnum getType(Request request) {
    String type = request.getSignType();
    if (type == null || type.trim().length() == 0) {
      return null;
    }
    return Enum.valueOf(SignatureEnum.class, type);
  }

}
