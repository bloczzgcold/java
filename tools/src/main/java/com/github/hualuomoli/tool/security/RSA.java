package com.github.hualuomoli.tool.security;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.github.hualuomoli.tool.InvalidDataException;

/**
 * RSA签名验签
 */
public class RSA {

  private static final String algorithm = "SHA1withRSA";
  private static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * 签名 {@linkplain #DEFAULT_CHARSET}
   * @param privateKey 私钥
   * @param origin 签名原文
   * @return 签名
   */
  public static String sign(String privateKey, String origin) {
    return sign(privateKey, origin, DEFAULT_CHARSET);
  }

  /**
   * 签名
   * @param privateKey 私钥
   * @param origin 签名原文
   * @param charset 数据编码
   * @return 签名
   */
  public static String sign(String privateKey, String origin, String charset) {
    try {
      return Base64.encode(sign(Base64.decode(privateKey), origin.getBytes(charset)));
    } catch (UnsupportedEncodingException e) {
      throw new InvalidDataException(e);
    }
  }

  /**
   * 验证签名 {@linkplain #DEFAULT_CHARSET}
   * @param publicKey 公钥
   * @param origin 签名原文
   * @param sign 签名
   * @return 签名是否合法
   */
  public static boolean verify(String publicKey, String origin, String sign) {
    return verify(publicKey, origin, sign, DEFAULT_CHARSET);
  }

  /**
  * 验证签名
  * @param publicKey 公钥
  * @param origin 签名原文
  * @param sign 签名
  * @param charset 数据编码
  * @return 签名是否合法
  */
  public static boolean verify(String publicKey, String origin, String sign, String charset) {
    try {
      return verify(Base64.decode(publicKey), origin.getBytes(charset), Base64.decode(sign));
    } catch (UnsupportedEncodingException e) {
      throw new InvalidDataException(e);
    }
  }

  /**
  * 签名
  * @param privateKey 私钥
  * @param origin 签名原文
  * @return 签名
  */
  public static byte[] sign(byte[] privateKey, byte[] origin) {
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
      throw new InvalidDataException(e);
    }
    // end
  }

  /**
  * 验证签名
  * @param publicKey 公钥
  * @param origin 签名原文
  * @param sign 签名
  * @return 签名是否合法
  */
  public static boolean verify(byte[] publicKey, byte[] origin, byte[] sign) {
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
      throw new InvalidDataException(e);
    }
    // end
  }

}
