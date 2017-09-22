package com.github.hualuomoli.gateway.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.hualuomoli.gateway.client.entity.HttpHeader;
import com.github.hualuomoli.gateway.client.entity.Request;
import com.github.hualuomoli.gateway.client.entity.Response;
import com.github.hualuomoli.gateway.client.http.HttpInvoker;
import com.github.hualuomoli.gateway.client.http.HttpInvoker.HttpResult;
import com.github.hualuomoli.gateway.client.interceptor.Interceptor;
import com.github.hualuomoli.gateway.client.lang.BusinessException;
import com.github.hualuomoli.gateway.client.lang.ClientException;
import com.github.hualuomoli.gateway.client.parser.JSONParser;
import com.github.hualuomoli.gateway.client.parser.Parser;

/**
 * 客户端
 */
public class GatewayClient<Req extends Request, Res extends Response> {

  private static final ThreadLocal<List<HttpHeader>> REQUEST_HEADER_LOCAL = new ThreadLocal<List<HttpHeader>>();
  private static final ThreadLocal<List<HttpHeader>> RESPONSE_HEADER_LOCAL = new ThreadLocal<List<HttpHeader>>();

  private Parser parser;
  protected JSONParser jsonParser;
  private HttpInvoker httpInvoker;
  private List<Interceptor> interceptors = new ArrayList<Interceptor>();

  private String url;
  private String partnerId;
  private Class<Req> requestClazz;
  private Class<Res> responseClazz;

  public GatewayClient(String url, String partnerId, Class<Req> requestClazz, Class<Res> responseClazz) {
    super();
    this.url = url;
    this.partnerId = partnerId;
    this.requestClazz = requestClazz;
    this.responseClazz = responseClazz;
  }

  public GatewayClient<Req, Res> setParser(Parser parser) {
    this.parser = parser;
    return this;
  }

  public GatewayClient<Req, Res> setJsonParser(JSONParser jsonParser) {
    this.jsonParser = jsonParser;
    return this;
  }

  public GatewayClient<Req, Res> setHttpInvoker(HttpInvoker httpInvoker) {
    this.httpInvoker = httpInvoker;
    return this;
  }

  public GatewayClient<Req, Res> setInterceptors(List<Interceptor> interceptors) {
    this.interceptors = interceptors;
    return this;
  }

  public GatewayClient<Req, Res> addInterceptor(Interceptor interceptor) {
    interceptors.add(interceptor);
    return this;
  }

  public void setRequestHeaders(List<HttpHeader> requestHeaders) {
    REQUEST_HEADER_LOCAL.set(requestHeaders);
  }

  public GatewayClient<Req, Res> addRequestHeader(HttpHeader requestHeader) {
    this.getLocalRequestHeader().add(requestHeader);
    return this;
  }

  /**
   * 获取当前线程请求header
   * @return header
   */
  private List<HttpHeader> getLocalRequestHeader() {
    List<HttpHeader> requestHeaders = REQUEST_HEADER_LOCAL.get();
    if (requestHeaders == null) {
      requestHeaders = new ArrayList<HttpHeader>();
      REQUEST_HEADER_LOCAL.set(requestHeaders);
    }
    return requestHeaders;
  }

  /**
   * 获取当前线程请求header
   * @return header
   */
  public List<HttpHeader> getLocalResponseHeader() {
    List<HttpHeader> responseHeaders = RESPONSE_HEADER_LOCAL.get();
    if (responseHeaders == null) {
      responseHeaders = new ArrayList<HttpHeader>();
      REQUEST_HEADER_LOCAL.set(responseHeaders);
    }
    return responseHeaders;
  }

  /**
   * 执行
   * @param method 请求方法
   * @param bizContent 请求业务内容
   * @return 业务返回结果
   * @throws BusinessException 业务处理异常
   * @throws ClientException 客户端调用异常
   */
  public String execute(String method, String bizContent) throws BusinessException, ClientException {
    Req request = this.newInstance(requestClazz);
    request.setPartnerId(partnerId);
    request.setMethod(method);
    request.setBizContent(bizContent);

    Res response = null;
    try {

      // 前置拦截
      for (int i = 0, size = interceptors.size(); i < size; i++) {
        interceptors.get(i).preHandle(partnerId, request);
      }

      // 执行业务处理
      String content = parser.getRequestContent(request);
      HttpResult httpResult = httpInvoker.invoke(url, content, this.getLocalRequestHeader());
      this.getLocalResponseHeader().addAll(httpResult.getHeaders()); // 设置响应header
      String result = httpResult.getResult();
      response = parser.parse(result, responseClazz);

      // 调用失败
      if (!response.callSuccess()) {
        throw new BusinessException(response);
      }

      // 网关错误
      if (!response.success()) {
        throw new ClientException(response);
      }

      // 后置拦截
      for (int size = interceptors.size(), i = size - 1; i >= 0; i--) {
        interceptors.get(i).postHandle(partnerId, request, response);
      }
      return response.getResult();
    } catch (IOException e) {
      throw new ClientException(e);
    } catch (Exception e) {
      throw new ClientException(e);
    }
    // end
  }

  /**
   * 实例化
   * @param clazz 类类型
   * @return 实例
   */
  private <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end method
  }

}
