package com.github.hualuomoli.gateway.api.entity;

import com.github.hualuomoli.gateway.api.enums.CodeEnum;

public class Response {

    /** 网关处理编码 */
    private CodeEnum code;
    /** 网关处理信息 */
    private String message;
    /** 业务处理编码 */
    private String subCode;
    /** 业务处理信息 */
    private String subMessage;
    /** 响应结果 */
    private String result;
    /** 签名 */
    private String sign;

    public CodeEnum getCode() {
        return code;
    }

    public void setCode(CodeEnum code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public void setSubMessage(String subMessage) {
        this.subMessage = subMessage;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Response [code=" + code + ", message=" + message + ", subCode=" + subCode + ", subMessage=" + subMessage + ", result=" + result + ", sign=" + sign + "]";
    }

}
