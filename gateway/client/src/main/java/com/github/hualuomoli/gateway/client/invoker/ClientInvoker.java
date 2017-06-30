package com.github.hualuomoli.gateway.client.invoker;

import com.github.hualuomoli.gateway.api.entity.Request;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.client.lang.ClientException;

/**
 * 客户端
 */
public interface ClientInvoker {

    /**
     * 调用
     * @param request 请求
     * @return 返回的业务结果
     * @throws BusinessException 业务处理错误
     * @throws ClientException 客户端调用错误
     */
    String call(Request request) throws BusinessException, ClientException;

}
