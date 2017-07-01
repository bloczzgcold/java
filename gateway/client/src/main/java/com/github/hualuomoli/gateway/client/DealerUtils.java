package com.github.hualuomoli.gateway.client;

import java.util.ArrayList;
import java.util.List;

import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.enums.SignatureEnum;
import com.github.hualuomoli.gateway.client.dealer.EncryptionDealer;
import com.github.hualuomoli.gateway.client.dealer.SignatureDealer;

public class DealerUtils {

  // 加解密处理类
  private static final List<EncryptionDealer> ENCRYPTION_DEALER = new ArrayList<EncryptionDealer>();
  // 签名处理类
  private static final List<SignatureDealer> SIGNATURE_DEALER = new ArrayList<SignatureDealer>();

  static void setEncryptionDealers(List<EncryptionDealer> encryptionDealers) {
    ENCRYPTION_DEALER.addAll(encryptionDealers);
  }

  static void setSignatureDealers(List<SignatureDealer> signatureDealers) {
    SIGNATURE_DEALER.addAll(signatureDealers);
  }

  /**
   * 获取加密/解密处理类
   * @param encryption 加密/解密类型
   * @return 处理类
   */
  public static EncryptionDealer getEncryptionDealer(EncryptionEnum encryption) {
    if (encryption == null) {
      return null;
    }
    if (ENCRYPTION_DEALER.size() == 0) {
      return null;
    }
    for (EncryptionDealer dealer : ENCRYPTION_DEALER) {
      if (dealer.support(encryption)) {
        return dealer;
      }
    }
    return null;
  }

  /**
   * 获取签名/验签处理类
   * @param signature 签名/验签类型
   * @return 处理类
   */
  public static SignatureDealer getSignatureDealer(SignatureEnum signature) {
    if (signature == null) {
      return null;
    }
    if (SIGNATURE_DEALER.size() == 0) {
      return null;
    }
    for (SignatureDealer dealer : SIGNATURE_DEALER) {
      if (dealer.support(signature)) {
        return dealer;
      }
    }
    return null;
  }

}
