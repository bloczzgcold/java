package com.github.hualuomoli.gateway.api.lang;

/**
 * 没有路由
 */
public class NoRouterException extends RuntimeException {

    private static final long serialVersionUID = 3414288695150102402L;

    private String method;
    private String version;

    public NoRouterException(String method, String version) {
        super();
        this.method = method;
        this.version = version;
    }

    public NoRouterException(String method, String version, Throwable cause) {
        super(cause);
        this.method = method;
        this.version = version;
    }

    public String method() {
        return method;
    }

    public String version() {
        return version;
    }

    @Override
    public String getMessage() {
        return "方法" + method + "未找到" + (version == null ? "" : ",version=" + version);
    }

}
