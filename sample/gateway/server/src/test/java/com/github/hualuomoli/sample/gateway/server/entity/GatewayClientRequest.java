package com.github.hualuomoli.sample.gateway.server.entity;

import com.github.hualuomoli.gateway.client.entity.Request;

public class GatewayClientRequest implements Request {

  /** 网关版本号 */
  private String version;
  /** 合作伙伴ID */
  private String partnerId;
  /** 请求方法 */
  private String method;
  /** 时间戳 yyyyMMdd HH:mm:ss */
  private String timestamp;
  /** 随机字符串 */
  private String nonceStr;
  /** 请求业务内容 */
  private String bizContent;
  /** 签名类型 */
  private String signType;
  /** 签名数据 */
  private String sign;
  /** 加密类型 */
  private String encryptType;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(String partnerId) {
    this.partnerId = partnerId;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getNonceStr() {
    return nonceStr;
  }

  public void setNonceStr(String nonceStr) {
    this.nonceStr = nonceStr;
  }

  public String getBizContent() {
    return bizContent;
  }

  public void setBizContent(String bizContent) {
    this.bizContent = bizContent;
  }

  public String getSignType() {
    return signType;
  }

  public void setSignType(String signType) {
    this.signType = signType;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getEncryptType() {
    return encryptType;
  }

  public void setEncryptType(String encryptType) {
    this.encryptType = encryptType;
  }

  @Override
  public String toString() {
    return "GatewayClientRequest [version=" + version + ", partnerId=" + partnerId + ", method=" + method + ", timestamp=" + timestamp + ", nonceStr="
        + nonceStr + ", bizContent=" + bizContent + ", signType=" + signType + ", sign=" + sign + ", encryptType=" + encryptType + "]";
  }

}
