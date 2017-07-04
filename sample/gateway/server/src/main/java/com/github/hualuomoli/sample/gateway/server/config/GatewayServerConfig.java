package com.github.hualuomoli.sample.gateway.server.config;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoAuthorityException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.api.support.security.AES;
import com.github.hualuomoli.gateway.api.support.security.RSA;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.business.interceptor.AuthorityInterceptor;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.business.local.Local;
import com.github.hualuomoli.gateway.server.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.server.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.interceptor.encrypt.EncryptionInterceptor;
import com.github.hualuomoli.gateway.server.interceptor.sign.SignatureInterceptor;
import com.github.hualuomoli.sample.gateway.server.biz.gateway.service.BusinessHandlerService;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.google.common.collect.Lists;

@Configuration
public class GatewayServerConfig {

  @Autowired
  private BusinessHandlerService businessHandler;

  @Bean
  public GatewayServer initGateway() {

    GatewayServer server = new GatewayServer();
    server.setBusinessHandler(businessHandler);
    server.setAuthorityInterceptor(this.authorityInterceptor());
    server.setInterceptors(this.interceptors());
    server.setBusinessInterceptors(Lists.newArrayList(this.businessInterceptor()));

    return server;
  }

  private List<Interceptor> interceptors() {
    List<Interceptor> interceptors = Lists.newArrayList();

    // pre = 解密 - 验签 - 其他
    // post = 其他 - 签名 + 加密
    interceptors.add(new EncryptionInterceptor(Lists.newArrayList(this.encryptionDealer())));
    interceptors.add(new SignatureInterceptor(Lists.newArrayList(this.signatureDealer())));

    interceptors.add(new Interceptor() {

      final Logger logger = LoggerFactory.getLogger(Interceptor.class);

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, Request request) throws NoPartnerException, InvalidDataException {
        logger.debug("请求业务内容={}", request.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Request request, Response response) {
        logger.debug("响应业务内容={}", response.getResult());
      }
    });

    return interceptors;
  }

  private AuthorityInterceptor authorityInterceptor() {
    return new AuthorityInterceptor() {

      @Override
      public void handle(HttpServletRequest req, HttpServletResponse res) throws NoAuthorityException {
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
      public String decrypt(String data, String partnerId) throws NoPartnerException, InvalidDataException {
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
        return RSA.sign(Key.SERVER_PRIVATE_KEY, origin);
      }

      @Override
      public boolean verify(String origin, String sign, String partnerId) throws NoPartnerException, InvalidDataException {
        return RSA.verify(Key.CLIENT_PUBLIC_KEY, origin, sign);
      }
    };
  }

  private BusinessInterceptor businessInterceptor() {
    final Logger logger = LoggerFactory.getLogger(BusinessInterceptor.class);

    return new BusinessInterceptor() {

      @Override
      public void preHandle(HttpServletRequest req, HttpServletResponse res, Method method, Object handler, Object[] params) {
        logger.debug("业务处理前日志输出 partnerId={},method={},bizContent={}", Local.getPartnerId(), Local.getMethod(), Local.getBizContent());
      }

      @Override
      public void postHandle(HttpServletRequest req, HttpServletResponse res, Object result) {
        logger.debug("业务处理后日志输出 partnerId={},method={},result={}", Local.getPartnerId(), Local.getMethod(), result);
      }

      @Override
      public void afterCompletion(HttpServletRequest req, HttpServletResponse res, BusinessException be) {
        logger.debug("业务处理出现异常 partnerId={},method={},e={}", Local.getPartnerId(), Local.getMethod(), be.getMessage(), be);
      }
    };
  }
}
