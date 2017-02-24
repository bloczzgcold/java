package com.github.hualuomoli.tool;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AES加解密
 *
 */
public class AES {

	private static final Logger logger = LoggerFactory.getLogger(AES.class);

	private static final String algorithm = "AES/ECB/PKCS5Padding";
	private static final Charset CHARSET = Charset.forName("UTF-8");

	/**
	 * 加密,默认UTF-8
	 * @param keyBase64 Base64位key
	 * @param content	待加密内容
	 * @return Base64密文
	 */
	public static String encryptBase64(String keyBase64, String content) {
		return encryptBase64(keyBase64, content, CHARSET);
	}

	/**
	 * 加密
	 * @param keyBase64 Base64位key
	 * @param content	待加密内容
	 * @param charset	代价密内容编码集
	 * @return Base64密文
	 */
	public static String encryptBase64(String keyBase64, String content, Charset charset) {

		if (keyBase64 == null || content == null || charset == null) {
			return null;
		}

		try {
			// 获取key
			SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(keyBase64), "AES");

			// 加密
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] data = cipher.doFinal(content.getBytes(charset));

			return Base64.encode(data);
		} catch (Exception e) {
			logger.debug("无法加密", e);
		}

		return null;
	}

	/**
	 * 解密,默认UTF-8编码
	 * @param keyBase64		Base64的key
	 * @param contentBase64	Base64的加密内容	
	 * @return 明文
	 */
	public static String decrypt(String keyBase64, String contentBase64) {
		return decrypt(keyBase64, contentBase64, CHARSET);
	}

	/**
	 * 解密
	 * @param keyBase64		Base64的key
	 * @param contentBase64	Base64的加密内容	
	 * @param charset		明文数据编码集
	 * @return 明文
	 */
	public static String decrypt(String keyBase64, String contentBase64, Charset charset) {
		if (keyBase64 == null || contentBase64 == null || charset == null) {
			return null;
		}

		try {

			// 获取key
			SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(keyBase64), "AES");

			// 解密
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] data = cipher.doFinal(Base64.decode(contentBase64));

			return new String(data, charset);
		} catch (Exception e) {
			logger.debug("无法解密", e);
		}

		return null;
	}

}
