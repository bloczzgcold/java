package com.github.hualuomoli.gateway.client.android.security;

import java.nio.charset.Charset;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.security.Base64;

/**
 * AES加解密
 *
 */
public class AES implements com.github.hualuomoli.gateway.client.security.AES {

	private static final Logger logger = LoggerFactory.getLogger(AES.class);

	private static final String algorithm = "AES";

	private Charset charset;
	private String transformation;

	public AES() {
		super();
		this.charset = Charset.forName("UTF-8");
		this.transformation = "AES/ECB/PKCS5Padding";
	}

	public AES(Charset charset) {
		super();
		this.charset = charset;
		this.transformation = "AES/ECB/PKCS5Padding";
	}

	public AES(String transformation) {
		super();
		this.charset = Charset.forName("UTF-8");
		this.transformation = transformation;
	}

	public AES(Charset charset, String transformation) {
		super();
		this.charset = charset;
		this.transformation = transformation;
	}

	@Override
	public byte[] encrypt(Key key, byte[] content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 加密
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(content);
		} catch (Exception e) {
			logger.debug("[encrypt]", e);
		}

		return null;
	}

	@Override
	public String encrypt(Key key, String content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 获取数据
		byte[] contentBytes = content.getBytes(charset);

		// 执行加密
		byte[] cipherContent = this.encrypt(key, contentBytes);

		// 转换成Base64
		return Base64.encode(cipherContent);
	}

	@Override
	public byte[] encrypt(byte[] key, byte[] content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 获取key
		SecretKey secretKey = new SecretKeySpec(key, algorithm);

		// 执行加密
		return this.encrypt(secretKey, content);
	}

	@Override
	public String encrypt(byte[] key, String content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 获取key
		SecretKey secretKey = new SecretKeySpec(key, algorithm);

		// 获取数据
		byte[] contentBytes = content.getBytes(charset);

		// 执行加密
		byte[] cipherContent = this.encrypt(secretKey, contentBytes);

		// 转换成Base64
		return Base64.encode(cipherContent);
	}

	@Override
	public byte[] encrypt(String key, byte[] content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 获取key
		byte[] keyBytes = Base64.decode(key);
		SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

		// 执行加密
		return this.encrypt(secretKey, content);
	}

	@Override
	public String encrypt(String key, String content) {

		if (key == null) {
			return null;
		}

		if (content == null) {
			return null;
		}

		// 获取key
		byte[] keyBytes = Base64.decode(key);
		SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

		// 获取数据
		byte[] contentBytes = content.getBytes(charset);

		// 执行加密
		byte[] cipherContent = this.encrypt(secretKey, contentBytes);

		// 转换成Base64
		return Base64.encode(cipherContent);
	}

	@Override
	public byte[] decrypt(Key key, byte[] cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 解密
		try {
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(cipherContent);
		} catch (Exception e) {
			logger.debug("[decrypt]", e);
		}

		return null;
	}

	@Override
	public String decrypt(Key key, String cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 获取数据
		byte[] cipherContentBytes = Base64.decode(cipherContent);

		// 执行解密
		byte[] content = this.decrypt(key, cipherContentBytes);

		// 转换成字符串
		return new String(content, charset);
	}

	@Override
	public byte[] decrypt(byte[] key, byte[] cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 获取key
		SecretKey secretKey = new SecretKeySpec(key, algorithm);

		// 执行解密
		return this.decrypt(secretKey, cipherContent);
	}

	@Override
	public String decrypt(byte[] key, String cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 获取key
		SecretKey secretKey = new SecretKeySpec(key, algorithm);

		// 获取数据
		byte[] cipherContentBytes = Base64.decode(cipherContent);

		// 执行解密
		byte[] content = this.decrypt(secretKey, cipherContentBytes);

		// 转换成字符串
		return new String(content, charset);
	}

	@Override
	public byte[] decrypt(String key, byte[] cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 获取key
		byte[] keyBytes = Base64.decode(key);
		SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

		// 执行解密
		return this.decrypt(secretKey, cipherContent);
	}

	@Override
	public String decrypt(String key, String cipherContent) {

		if (key == null) {
			return null;
		}

		if (cipherContent == null) {
			return null;
		}

		// 获取key
		byte[] keyBytes = Base64.decode(key);
		SecretKey secretKey = new SecretKeySpec(keyBytes, algorithm);

		// 获取数据
		byte[] cipherContentBytes = Base64.decode(cipherContent);

		// 执行解密
		byte[] content = this.decrypt(secretKey, cipherContentBytes);

		// 转换成字符串
		return new String(content, charset);
	}

}
