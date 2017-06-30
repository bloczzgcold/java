package com.github.hualuomoli.gateway.client.lang;

import com.github.hualuomoli.gateway.api.enums.CodeEnum;

/**
 * 客户端调用错误
 */
public class ClientException extends RuntimeException {

    private static final long serialVersionUID = -6529281280421689975L;

    private CodeEnum code;

    public ClientException(CodeEnum code, String message) {
        super(message);
        this.code = code;
    }

    public ClientException(CodeEnum code, Exception e) {
        super(e);
        this.code = code;
    }

    public CodeEnum getCode() {
        return code;
    }

}
