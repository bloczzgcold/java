package com.github.hualuomoli.gateway.client.security;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * RSA签名验签
 *
 */
public interface RSA {

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	byte[] sign(PrivateKey privateKey, byte[] data);

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	String sign(PrivateKey privateKey, String data);

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	byte[] sign(byte[] privateKey, byte[] data);

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	String sign(byte[] privateKey, String data);

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	byte[] sign(String privateKey, byte[] data);

	/**
	 * 签名
	 * @param privateKey 私钥
	 * @param data 明文
	 * @return 签名
	 */
	String sign(String privateKey, String data);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(PublicKey publicKey, byte[] data, byte[] signature);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(PublicKey publicKey, String data, String signature);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(byte[] publicKey, byte[] data, byte[] signature);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(byte[] publicKey, String data, String signature);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(String publicKey, byte[] data, byte[] signature);

	/**
	 * 验证签名
	 * @param publicKey 公钥
	 * @param data 明文
	 * @param signature 签名
	 * @return 签名是否合法
	 */
	boolean verify(String publicKey, String data, String signature);

}
