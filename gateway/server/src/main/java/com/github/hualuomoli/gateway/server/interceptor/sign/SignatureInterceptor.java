package com.github.hualuomoli.gateway.server.interceptor.sign;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;

/**
 * 签名/验签拦截器
 */
public class SignatureInterceptor implements Interceptor {

  private static final Logger logger = LoggerFactory.getLogger(SignatureInterceptor.class);

  private List<SignatureDealer> dealers = new ArrayList<SignatureDealer>();

  public SignatureInterceptor() {
  }

  public SignatureInterceptor(List<SignatureDealer> dealers) {
    this.dealers = dealers;
  }

  public void setDealers(List<SignatureDealer> dealers) {
    this.dealers = dealers;
  }

  @Override
  public void preHandle(HttpServletRequest req, HttpServletResponse res, Request request) throws NoPartnerException, InvalidDataException {

    // 获取处理类
    SignatureDealer dealer = this.getDealer(request);

    // 获取签名原文
    StringBuilder buffer = new StringBuilder();
    List<Data> datas = this.getDatas(request);
    for (Data data : datas) {
      buffer.append("&").append(data.name).append("=").append(data.value);
    }
    String origin = buffer.toString().substring(1);
    logger.debug("请求签名原文={}", origin);

    boolean success = dealer.verify(origin, request.getSign(), request.getPartnerId());
    if (!success) {
      throw new InvalidDataException("invalid sign data.");
    }
    // end

  }

  @Override
  public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {

    // 获取处理类
    SignatureDealer dealer = this.getDealer(request);

    // 获取签名原文
    StringBuilder buffer = new StringBuilder();
    List<Data> datas = this.getDatas(response);
    for (Data data : datas) {
      buffer.append("&").append(data.name).append("=").append(data.value);
    }
    String origin = buffer.toString().substring(1);
    logger.debug("响应签名原文={}", origin);

    String sign = dealer.sign(origin, request.getPartnerId());
    response.setSign(sign);
  }

  /**
   * 获取签名原文,忽略签名sign
   * @param object 参数信息
   * @return 待签名信息
   */
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
        String val = String.valueOf(value);
        if (val.trim().length() == 0) {
          continue;
        }
        datas.add(new Data(name, val));
      } catch (Exception e) {
        logger.debug(e.getMessage(), e);
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

    Data(String name, String value) {
      super();
      this.name = name;
      this.value = value;
    }
  }

  /**
   * 获取签名/验签处理类,如果未设置签名类型或签名类型处理器未配置,抛出异常
   * @param request 请求信息
   * @return 处理类
   */
  private SignatureDealer getDealer(Request request) {
    // get sign type
    String type = request.getSignType();
    if (type == null || type.trim().length() == 0) {
      throw new InvalidDataException("please configure sign type.");
    }

    // get dealer
    SignatureEnum signature = Enum.valueOf(SignatureEnum.class, type);
    for (SignatureDealer dealer : dealers) {
      if (dealer.support(signature)) {
        return dealer;
      }
    }
    throw new InvalidDataException("there is no dealer support " + signature);
  }

}
