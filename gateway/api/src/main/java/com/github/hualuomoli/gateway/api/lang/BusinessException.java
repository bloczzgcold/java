package com.github.hualuomoli.gateway.api.lang;

/**
 * 业务异常
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1762069845484506953L;

    /** 异常编码 */
    private String subCode;
    /** 异常信息 */
    private String subMessage;
    /** 异常描述 */
    private String subDescription;

    public BusinessException(String subCode, String subMessage) {
        super();
        this.subCode = subCode;
        this.subMessage = subMessage;
    }

    public BusinessException(String subCode, String subMessage, Throwable t) {
        super(t);
        this.subCode = subCode;
        this.subMessage = subMessage;
    }

    public BusinessException(String subCode, String subMessage, String subDescription) {
        super();
        this.subCode = subCode;
        this.subMessage = subMessage;
        this.subDescription = subDescription;
    }

    public BusinessException(String subCode, String subMessage, String subDescription, Throwable t) {
        super(t);
        this.subCode = subCode;
        this.subMessage = subMessage;
        this.subDescription = subDescription;
    }

    public String getSubCode() {
        return subCode;
    }

    public String getSubMessage() {
        return subMessage;
    }

    public String getSubDescription() {
        return subDescription;
    }

}
