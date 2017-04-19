package com.github.hualuomoli.gateway.client.security;

import java.security.Key;

/**
 * AES加解密
 *
 */
public interface AES {

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	byte[] encrypt(Key key, byte[] content);

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	String encrypt(Key key, String content);

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	byte[] encrypt(byte[] key, byte[] content);

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	String encrypt(byte[] key, String content);

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	byte[] encrypt(String key, byte[] content);

	/**
	 * 加密
	 * @param key 加密key
	 * @param content 明文
	 * @return 密文
	 */
	String encrypt(String key, String content);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	byte[] decrypt(Key key, byte[] cipherContent);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	String decrypt(Key key, String cipherContent);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	byte[] decrypt(byte[] key, byte[] cipherContent);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	String decrypt(byte[] key, String cipherContent);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	byte[] decrypt(String key, byte[] cipherContent);

	/**
	 * 解密
	 * @param key 解密key
	 * @param cipherContent 密文
	 * @return 明文
	 */
	String decrypt(String key, String cipherContent);

}
