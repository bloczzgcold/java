package com.github.hualuomoli.gateway.api.lang;

/**
 * 合作伙伴未找到
 */
public class NoPartnerException extends RuntimeException {

  private static final long serialVersionUID = 7115566411057573557L;

  private String partnerId;

  public NoPartnerException(String partnerId) {
    super();
    this.partnerId = partnerId;
  }

  public NoPartnerException(String partnerId, Throwable e) {
    super(e);
    this.partnerId = partnerId;
  }

  public String getPartnerId() {
    return partnerId;
  }

  @Override
  public String getMessage() {
    return "合作伙伴" + partnerId + "未注册";
  }

}
