package com.github.hualuomoli.gateway.android.http;

import com.github.hualuomoli.gateway.android.entity.Request;
import com.github.hualuomoli.gateway.android.entity.Response;
import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;
import com.github.hualuomoli.gateway.android.json.JSONParser;
import com.github.hualuomoli.gateway.android.lang.GatewayException;

/**
 * 执行HTTP请求
 */
public interface HttpInvoker {

  /**
   * 执行HTTP请求
   * @param url 请求服务器地址
   * @param request 请求信息
   * @param jsonParser JSON转换器
   * @return 返回执行结果
   * @throws GatewayException HTTP调用错误
   * 服务器未响应{@linkplain ErrorTypeEnum#HTTP_SERVER_NOT_FOUND}
   * 服务器运行错误{@linkplain ErrorTypeEnum#HTTP_SERVER_RUNTIME_ERROR}
   * 服务器未知错误{@linkplain ErrorTypeEnum#HTTP_SERVER_OTHER_ERROR}
   */
  Response execute(String url, Request request, JSONParser jsonParser) throws GatewayException;

}
