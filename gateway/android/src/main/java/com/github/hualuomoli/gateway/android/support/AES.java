package com.github.hualuomoli.gateway.android.support;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加解密
 *
 */
public class AES {

  private static final String algorithm = "AES/ECB/PKCS5Padding";

  /**
   * 加密
   * @param key Key
   * @param content 明文
   * @return 密文
   */
  public static byte[] encrypt(byte[] key, byte[] content) {
    if (key == null || content == null) {
      return null;
    }
    try {
      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

      // 加密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.ENCRYPT_MODE, secretKey);
      return cipher.doFinal(content);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end
  }

  /**
   * 加密
   * @param key Key
   * @param cipherContent 密文
   * @return 明文
   */
  public static byte[] decrypt(byte[] key, byte[] cipherContent) {
    if (key == null || cipherContent == null) {
      return null;
    }
    try {

      // 获取key
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

      // 解密
      Cipher cipher = Cipher.getInstance(algorithm);
      cipher.init(Cipher.DECRYPT_MODE, secretKey);
      return cipher.doFinal(cipherContent);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // end
  }

}
