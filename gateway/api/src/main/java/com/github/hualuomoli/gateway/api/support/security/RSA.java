package com.github.hualuomoli.gateway.api.support.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.github.hualuomoli.gateway.api.lang.InvalidDataException;

/**
 * RSA签名验签
 *
 */
public class RSA {

  private static final String algorithm = "SHA1withRSA";
  private static final String CHARSET = "UTF-8";

  /**
   * 签名
   * @param privateKey 私钥
   * @param origin 签名明文
   * @return 签名
   */
  public static String sign(String privateKey, String origin) {
    try {
      // 获取私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

      // 签名
      Signature signature = Signature.getInstance(algorithm);
      signature.initSign(priKey);
      signature.update(origin.getBytes(CHARSET));
      byte[] signData = signature.sign();

      return Base64.encode(signData);
    } catch (Exception e) {
      throw new InvalidDataException(e);
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
  public static boolean verify(String publicKey, String origin, String sign) {
    try {
      // 获取公钥
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

      // 验证签名
      Signature verifier = Signature.getInstance(algorithm);
      verifier.initVerify(pubKey);
      verifier.update(origin.getBytes(CHARSET));
      return verifier.verify(Base64.decode(sign));
    } catch (Exception e) {
      throw new InvalidDataException(e);
    }
    // end
  }

}
