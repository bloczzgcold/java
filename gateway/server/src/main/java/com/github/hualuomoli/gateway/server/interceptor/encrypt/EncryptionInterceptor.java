package com.github.hualuomoli.gateway.server.interceptor.encrypt;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;

/**
 * 加解密拦截器
 */
public class EncryptionInterceptor implements Interceptor {

  private static final Logger logger = LoggerFactory.getLogger(EncryptionInterceptor.class);

  private List<EncryptionDealer> dealers = new ArrayList<EncryptionDealer>();

  public EncryptionInterceptor() {
  }

  public EncryptionInterceptor(List<EncryptionDealer> dealers) {
    this.dealers = dealers;
  }

  public void setDealers(List<EncryptionDealer> dealers) {
    this.dealers = dealers;
  }

  @Override
  public void preHandle(HttpServletRequest req, HttpServletResponse res, Request request) throws NoPartnerException, InvalidDataException {

    // 获取处理类
    EncryptionDealer dealer = this.getEncryptionDealer(request);
    if (dealer == null) {
      return;
    }

    // 请求业务内容
    String bizContent = request.getBizContent();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    logger.debug("请求密文={}", bizContent);
    bizContent = dealer.decrypt(bizContent, partnerId);
    logger.debug("请求明文={}", bizContent);

    // 重新设置请求数据
    request.setBizContent(bizContent);
  }

  @Override
  public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {

    // 获取处理类
    EncryptionDealer dealer = this.getEncryptionDealer(request);
    if (dealer == null) {
      return;
    }

    // 响应数据
    String result = response.getResult();
    // 合作伙伴ID
    String partnerId = request.getPartnerId();

    logger.debug("响应明文={}", result);
    result = dealer.encrypt(result, partnerId);
    logger.debug("响应密文={}", result);

    // 重新设置响应数据
    response.setResult(result);
  }

  /**
   * 获取加密/解密处理类
   * @param request 请求信息
   * @return 处理类
   */
  private EncryptionDealer getEncryptionDealer(Request request) {
    String type = request.getEncryptType();
    if (type == null || type.trim().length() == 0) {
      return null;
    }

    EncryptionEnum encryption = Enum.valueOf(EncryptionEnum.class, type);

    for (EncryptionDealer dealer : dealers) {
      if (dealer.support(encryption)) {
        return dealer;
      }
    }
    throw new InvalidDataException("there is no dealer support " + encryption);
  }

}
