package com.github.hualuomoli.gateway.api.support.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.github.hualuomoli.gateway.api.lang.InvalidDataException;

/**
 * AES加解密
 *
 */
public class AES {

  private static final String algorithm = "AES/ECB/PKCS5Padding";
  private static final String CHARSET = "UTF-8";

  /**
   * 加密
   * @param key Key
   * @param content 明文
   * @return 密文
   */
  public static String encrypt(String key, String content) {
    if (content == null || content.length() == 0) {
      return content;
    }
    try {
      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(key), "AES");

      // 加密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      byte[] data = cipher.doFinal(content.getBytes(CHARSET));

      return Base64.encode(data);
    } catch (Exception e) {
      throw new InvalidDataException(e);
    }
    // end
  }

  /**
   * 加密
   * @param key Key
   * @param cipherContent 密文
   * @return 明文
   */
  public static String decrypt(String key, String cipherContent) {
    if (cipherContent == null || cipherContent.length() == 0) {
      return cipherContent;
    }
    try {

      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(key), "AES");

      // 解密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      byte[] data = cipher.doFinal(Base64.decode(cipherContent));

      return new String(data, CHARSET);
    } catch (Exception e) {
      throw new InvalidDataException(e);
    }
    // end
  }

}
