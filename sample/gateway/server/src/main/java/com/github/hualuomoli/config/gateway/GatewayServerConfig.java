package com.github.hualuomoli.config.gateway;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.lang.NoPartnerException;
import com.github.hualuomoli.gateway.api.support.security.AES;
import com.github.hualuomoli.gateway.api.support.security.RSA;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.business.BusinessHandler;
import com.github.hualuomoli.gateway.server.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.server.dealer.SignatureDealer;
import com.github.hualuomoli.gateway.server.interceptor.Interceptor;
import com.github.hualuomoli.gateway.server.interceptor.encrypt.EncryptionInterceptor;
import com.github.hualuomoli.gateway.server.interceptor.sign.SignatureInterceptor;
import com.github.hualuomoli.sample.gateway.server.key.Key;
import com.google.common.collect.Lists;

@Configuration(value = "com.github.hualuomoli.config.gateway.GatewayServerConfig")
@Import(value = { GatewayBusinessHandlerConfig.class })
public class GatewayServerConfig {

  @Autowired
  private BusinessHandler businessHandler;

  @Bean
  public GatewayServer initGateway() {

    GatewayServer server = new GatewayServer();
    server.setBusinessHandler(businessHandler);
    server.setInterceptors(this.interceptors());

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

}
