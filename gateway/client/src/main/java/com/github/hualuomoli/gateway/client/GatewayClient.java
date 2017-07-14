package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.entity.Response;
import com.github.hualuomoli.gateway.api.enums.CodeEnum;
import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;
import com.github.hualuomoli.gateway.client.interceptor.encrypt.EncryptionInterceptor;
import com.github.hualuomoli.gateway.client.interceptor.sign.SignatureInterceptor;
import com.github.hualuomoli.gateway.client.invoker.ClientInvoker;
import com.github.hualuomoli.gateway.client.lang.ClientException;

public class GatewayClient {

  private String version;
  private String partnerId;

  protected JSONParser jsonParser;
  private ClientInvoker invoker;

  private SignatureEnum signature;
  private EncryptionEnum encryption;
  private List<Interceptor> interceptors = new ArrayList<Interceptor>();

  public GatewayClient(String version, String partnerId) {
    this.version = version;
    this.partnerId = partnerId;
  }

  public void setJsonParser(JSONParser jsonParser) {
    this.jsonParser = jsonParser;
  }

  public void setInvoker(ClientInvoker invoker) {
    this.invoker = invoker;
  }

  public GatewayClient addSignature(SignatureEnum signature) {
    this.signature = signature;
    return this;
  }

  public GatewayClient addEncryption(EncryptionEnum encryption) {
    this.encryption = encryption;
    return this;
  }

  public GatewayClient addSignatureInterceptor(SignatureInterceptor signatureInterceptor) {
    interceptors.add(signatureInterceptor);
    return this;
  }

  public GatewayClient addEncryptionInterceptor(EncryptionInterceptor encryptionInterceptor) {
    interceptors.add(encryptionInterceptor);
    return this;
  }

  public GatewayClient addInterceptor(Interceptor interceptor) {
    interceptors.add(interceptor);
    return this;
  }

  /**
   * {@linkplain ClientInvoker#call(Request)}
   */
  public String call(String method, String bizContent) throws BusinessException, ClientException {
    Request request = new Request();
    request.setVersion(version);
    request.setPartnerId(partnerId);
    request.setMethod(method);
    request.setTimestamp(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));
    request.setNonceStr(UUID.randomUUID().toString().substring(0, 12));
    request.setBizContent(bizContent);
    request.setSignType(signature == null ? null : signature.name());
    request.setEncryptType(encryption == null ? null : encryption.name());

    try {

      // 前置拦截
      for (int i = 0, size = interceptors.size(); i < size; i++) {
        interceptors.get(i).preHandle(request);
      }

      // 执行业务处理
      invoker.call(request);
      Response response = jsonParser.parseObject(invoker.getResult(), Response.class);

      // 网关处理错误
      if (response.getCode() != CodeEnum.SUCCESS) {
        throw new ClientException(response.getCode(), response.getMessage());
      }

      // 后置拦截
      for (int size = interceptors.size(), i = size - 1; i >= 0; i--) {
        interceptors.get(i).postHandle(request, response);
      }

      return response.getResult();
    } catch (IOException e) {
      throw new ClientException(CodeEnum.NETWORK, e);
    } catch (InvalidDataException e) {
      throw new ClientException(CodeEnum.INVALID_DATA, e);
    }
    // end
  }

}
