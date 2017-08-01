package com.github.hualuomoli.gateway.android.support;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA签名验签
 *
 */
public class RSA {

  private static final String algorithm = "SHA1withRSA";

  /**
   * 签名
   * @param privateKey 私钥
   * @param origin 签名明文
   * @return 签名
   */
  public static byte[] sign(byte[] privateKey, byte[] origin) {
    if (privateKey == null || origin == null) {
      return null;
    }
    try {
      // 获取私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

      // 签名
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(priKey);
      signature.update(origin);
      return signature.sign();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end
  }

  /**
   * 验证签名
   * @param publicKey 公钥
   * @param origin 签名明文
   * @param sign 签名
   * @return 签名是否合法
   */
  public static boolean verify(byte[] publicKey, byte[] origin, byte[] sign) {
    if (publicKey == null || origin == null || sign == null) {
      return false;
    }
    try {
      // 获取公钥
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

      // 验证签名
      Signature verifier = Signature.getInstance(algorithm);
      verifier.initVerify(pubKey);
      verifier.update(origin);
      return verifier.verify(sign);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end
  }

}
