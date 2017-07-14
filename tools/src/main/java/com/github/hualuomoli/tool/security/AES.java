package com.github.hualuomoli.tool.security;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.github.hualuomoli.tool.InvalidDataException;

/**
 * AES加解密
 */
public class AES {

  private static final String algorithm = "AES/ECB/PKCS5Padding";
  private static final String DEFAULT_CHARSET = "UTF-8";

  /**
   * 加密 {@linkplain #DEFAULT_CHARSET}
   * @param key Key
   * @param content 明文
   * @return 密文
   */
  public static String encrypt(String key, String content) {
    return encrypt(key, content, DEFAULT_CHARSET);
  }

  /**
   * 加密
   * @param key Key
   * @param content 明文
   * @param charset 数据编码
   * @return 密文
   */
  public static String encrypt(String key, String content, String charset) {
    try {
      return Base64.encode(encrypt(Base64.decode(key), content.getBytes(charset)));
    } catch (UnsupportedEncodingException e) {
      throw new InvalidDataException(e);
    }
  }

  /**
   * 解密 {@linkplain #DEFAULT_CHARSET}
   * @param key Key
   * @param cipherContent 密文
   * @return 明文
   */
  public static String decrypt(String key, String cipherContent) {
    return decrypt(key, cipherContent, DEFAULT_CHARSET);
  }

  /**
   * 解密
   * @param key Key
   * @param cipherContent 密文
   * @param charset 数据编码
   * @return 明文
   */
  public static String decrypt(String key, String cipherContent, String charset) {
    try {
      return new String(decrypt(Base64.decode(key), Base64.decode(cipherContent)), charset);
    } catch (UnsupportedEncodingException e) {
      throw new InvalidDataException(e);
    }
  }

  /**
  * 加密
  * @param key Key
  * @param content 明文
  * @return 密文
  */
  public static byte[] encrypt(byte[] key, byte[] content) {
    try {
      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

      // 加密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return cipher.doFinal(content);
    } catch (Exception e) {
      throw new InvalidDataException(e);
    }
    // end
  }

  /**
  * 解密
  * @param key Key
  * @param cipherContent 密文
  * @return 明文
  */
  public static byte[] decrypt(byte[] key, byte[] cipherContent) {
    try {

      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

      // 解密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return cipher.doFinal(cipherContent);
    } catch (Exception e) {
      throw new InvalidDataException(e);
    }
    // end
  }

}
