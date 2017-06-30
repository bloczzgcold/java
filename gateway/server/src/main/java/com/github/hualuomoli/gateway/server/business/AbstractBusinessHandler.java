package com.github.hualuomoli.gateway.server.business;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.api.lang.NoAuthorityException;
import com.github.hualuomoli.gateway.api.lang.NoRouterException;
import com.github.hualuomoli.gateway.api.parser.JSONParser;
import com.github.hualuomoli.gateway.server.business.interceptor.AuthorityInterceptor;
import com.github.hualuomoli.gateway.server.business.interceptor.BusinessInterceptor;
import com.github.hualuomoli.gateway.server.business.local.Local;

/**
 * 业务处理者
 */
public abstract class AbstractBusinessHandler implements BusinessHandler {

    private static final Logger logger = Logger.getLogger(AbstractBusinessHandler.class.getName());

    private Tool tool;
    private static boolean init = false;

    public void init() {
        if (init) {
            return;
        }
        tool = new Tool(this.parser());
        init = true;
        if (logger.isLoggable(Level.INFO)) {
            logger.info("init tool.");
        }
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res//
            , String partnerId, String method, String bizContent //
            , AuthorityInterceptor authorityInterceptor, List<BusinessInterceptor> interceptors) throws NoAuthorityException, NoRouterException, BusinessException {

        this.init();

        // 设置信息到本地线程
        Local.setPartnerId(partnerId);
        Local.setMethod(method);
        Local.setBizContent(bizContent);

        String version = this.parser().requestVersion(req);

        Function function = tool.getFunction(method, version);
        // 没有路由
        if (function == null) {
            throw new NoRouterException(method, version);
        }

        // 权限认证
        if (authorityInterceptor != null) {
            authorityInterceptor.handle(req, res);
        }

        List<Object> paramList = new ArrayList<Object>();
        Class<?>[] parameterTypes = function.method.getParameterTypes();
        c: for (Class<?> parameterType : parameterTypes) {

            // HTTP request
            if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
                paramList.add(req);
                continue;
            }

            // HTTP response
            if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
                paramList.add(res);
                continue;
            }

            // List
            if (List.class.isAssignableFrom(parameterType)) {
                ParameterizedType genericParameterTypes = (ParameterizedType) function.method.getGenericParameterTypes()[0];
                Class<?> clazz = (Class<?>) genericParameterTypes.getActualTypeArguments()[0];
                paramList.add(this.jsonParser().parseArray(bizContent, clazz));
                continue;
            }

            // packageName
            String[] packageNames = this.packageNames();
            String name = parameterType.getName();
            for (String packageName : packageNames) {
                if (name.startsWith(packageName)) {
                    paramList.add(this.jsonParser().parseObject(bizContent, parameterType));
                    continue c;
                }
            }

            // end
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("there is not support type" + name);
            }
            paramList.add(null);

        }

        // 请求参数
        Object[] params = paramList.toArray(new Object[] {});
        // 处理类
        Object handler = tool.getDealer(function);

        // 执行业务
        try {
            return this.deal(function.method, handler, params, interceptors, req, res);
        } catch (BusinessException be) {
            throw be;
        } catch (Throwable t) {
            throw this.parser().parse(t);
        }

    }

    /**
     * 业务处理
     * @param method 执行方法
     * @param handler 执行器
     * @param params 执行参数 
     * @param interceptors 拦截器
     * @param req HTTP请求
     * @param res HTTP响应
     * @return 业务处理结果
     */
    private String deal(Method method, Object handler, Object[] params, List<BusinessInterceptor> interceptors, HttpServletRequest req, HttpServletResponse res) {

        // 前置拦截
        for (BusinessInterceptor interceptor : interceptors) {
            interceptor.preHandle(req, res, method, handler, params);
        }

        // 业务处理
        String result = null;
        BusinessException be = null;
        try {
            Object object = method.invoke(handler, params);
            if (object != null) {
                result = this.jsonParser().toJsonString(object);
            }
        } catch (IllegalAccessException e) {
            be = this.parser().parse(e);
        } catch (IllegalArgumentException e) {
            be = this.parser().parse(e);
        } catch (InvocationTargetException e) {
            be = this.parser().parse(e.getTargetException());
        }

        if (be == null) {
            // 后置拦截
            for (BusinessInterceptor interceptor : interceptors) {
                interceptor.postHandle(req, res, result);
            }
        } else {
            // 错误拦截
            for (BusinessInterceptor interceptor : interceptors) {
                interceptor.afterCompletion(req, res, be);
            }
            throw be;
        }

        return result;
    }

    protected abstract Parser parser();

    protected abstract JSONParser jsonParser();

    protected String[] packageNames() {
        return new String[] { "com.github.hualuomoli" };
    };

}
