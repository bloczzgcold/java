package com.github.hualuomoli.gateway.client.java.security;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.security.Base64;

/**
 * RSA签名验签
 *
 */
public class RSA implements com.github.hualuomoli.gateway.client.security.RSA {

	private static final Logger logger = LoggerFactory.getLogger(RSA.class);

	private Charset charset;
	private String algorithm;

	public RSA() {
		super();
		this.charset = Charset.forName("UTF-8");
		this.algorithm = "SHA1withRSA";
	}

	public RSA(Charset charset) {
		super();
		this.charset = charset;
		this.algorithm = "SHA1withRSA";
	}

	public RSA(String algorithm) {
		super();
		this.algorithm = algorithm;
		this.charset = Charset.forName("UTF-8");
	}

	public RSA(Charset charset, String algorithm) {
		super();
		this.charset = charset;
		this.algorithm = algorithm;
	}

	@Override
	public byte[] sign(PrivateKey privateKey, byte[] data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 签名
		try {
			Signature signature = Signature.getInstance(algorithm);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (Exception e) {
			logger.debug("[sign]", e);
		}
		return null;
	}

	@Override
	public String sign(PrivateKey privateKey, String data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);

		// 执行签名

		byte[] sign = this.sign(privateKey, dataBytes);

		// 转换成Base64
		return Base64.encode(sign);
	}

	@Override
	public byte[] sign(byte[] privateKey, byte[] data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 获取私钥
		PrivateKey priKey = this.getPrivateKey(privateKey);
		if (priKey == null) {
			return null;
		}

		// 执行签名
		return this.sign(priKey, data);
	}

	@Override
	public String sign(byte[] privateKey, String data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 获取私钥
		PrivateKey priKey = this.getPrivateKey(privateKey);
		if (priKey == null) {
			return null;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);

		// 执行签名
		byte[] sign = this.sign(priKey, dataBytes);

		// 转换成Base64
		return Base64.encode(sign);
	}

	@Override
	public byte[] sign(String privateKey, byte[] data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 获取私钥
		byte[] privateKeyBytes = Base64.decode(privateKey);
		PrivateKey priKey = this.getPrivateKey(privateKeyBytes);
		if (priKey == null) {
			return null;
		}

		// 执行签名
		return this.sign(priKey, data);
	}

	@Override
	public String sign(String privateKey, String data) {

		if (privateKey == null) {
			return null;
		}

		if (data == null) {
			return null;
		}

		// 获取私钥
		byte[] privateKeyBytes = Base64.decode(privateKey);
		PrivateKey priKey = this.getPrivateKey(privateKeyBytes);
		if (priKey == null) {
			return null;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);

		// 执行签名
		byte[] sign = this.sign(priKey, dataBytes);

		// 转换成Base64
		return Base64.encode(sign);
	}

	@Override
	public boolean verify(PublicKey publicKey, byte[] data, byte[] signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 验证签名
		try {
			Signature verifier = Signature.getInstance(algorithm);
			verifier.initVerify(publicKey);
			verifier.update(data);
			return verifier.verify(signature);
		} catch (Exception e) {
			logger.debug("[verify]", e);
		}
		return false;
	}

	@Override
	public boolean verify(PublicKey publicKey, String data, String signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);
		// 获取签名
		byte[] signatureBytes = Base64.decode(signature);

		// 执行验证
		return this.verify(publicKey, dataBytes, signatureBytes);
	}

	@Override
	public boolean verify(byte[] publicKey, byte[] data, byte[] signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 获取公钥
		PublicKey pubKey = this.getPublicKey(publicKey);
		if (pubKey == null) {
			return false;
		}

		// 执行验证
		return this.verify(pubKey, data, signature);
	}

	@Override
	public boolean verify(byte[] publicKey, String data, String signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 获取公钥
		PublicKey pubKey = this.getPublicKey(publicKey);
		if (pubKey == null) {
			return false;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);
		// 获取签名
		byte[] signatureBytes = Base64.decode(signature);

		// 执行验证
		return this.verify(pubKey, dataBytes, signatureBytes);
	}

	@Override
	public boolean verify(String publicKey, byte[] data, byte[] signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 获取公钥
		byte[] publicKeyBytes = Base64.decode(publicKey);
		PublicKey pubKey = this.getPublicKey(publicKeyBytes);
		if (pubKey == null) {
			return false;
		}

		// 执行验证
		return this.verify(pubKey, data, signature);
	}

	@Override
	public boolean verify(String publicKey, String data, String signature) {

		if (publicKey == null) {
			return false;
		}

		if (data == null) {
			return false;
		}

		if (signature == null) {
			return false;
		}

		// 获取公钥
		byte[] publicKeyBytes = Base64.decode(publicKey);
		PublicKey pubKey = this.getPublicKey(publicKeyBytes);
		if (pubKey == null) {
			return false;
		}

		// 获取数据
		byte[] dataBytes = data.getBytes(charset);
		// 获取签名
		byte[] signatureBytes = Base64.decode(signature);

		// 执行验证
		return this.verify(pubKey, dataBytes, signatureBytes);
	}

	/**
	 * 获取私钥
	 * @param privateKey 私钥字节
	 * @return 私钥
	 */
	private PrivateKey getPrivateKey(byte[] privateKey) {
		// 获取私钥
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (Exception e) {
			logger.debug("[getPrivateKey]", e);
		}

		return null;

	}

	/**
	 * 获取公钥
	 * @param publicKey 公钥字节
	 * @return 公钥
	 */
	private PublicKey getPublicKey(byte[] publicKey) {
		// 获取公钥
		try {
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(x509KeySpec);
		} catch (Exception e) {
			logger.debug("[getPublicKey]", e);
		}

		return null;

	}

}
