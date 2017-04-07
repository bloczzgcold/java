package com.github.hualuomoli.demo.gateway.server.controller;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hualuomoli.gateway.client.RSAGatewayClient;
import com.github.hualuomoli.gateway.client.http.HttpURLClient;
import com.github.hualuomoli.gateway.client.json.JSON;

public class ClientControllerTest {

	protected static final Logger logger = LoggerFactory.getLogger(ClientControllerTest.class);

	protected static final RSAGatewayClient client;

	static {
		String serverURL = "http://localhost/test/gateway";
		String partnerId = "tester";
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJDbtC/LQthh7uaFsD7ExqYQKWWhye3r/J1ifLqngLmTT+V1jwJdokrPG7hSzB50h6NQzlEZ4PFl2LGuGYr0yJJdahxdaz6kfaDBro5a8DO7KmE4fAGEhqFXEjvvf+Gs/R90CayMHK5BUBM4xpQtwMbTq0QbNMfzcBhEsD/Lc/g7AgMBAAECgYBfalglox1Eqj1SWnzc24B9oeeiqg74SJj8kgLWb766fe4Cloy8YjCkVgdMQj1xUhCF4pQDl6gzWYKChssMXHA/9b0YvSRFsCc32e/cqqApSGdeHKzhVS418ojc4LckCgq3fTtZPKxt8S1HG4QcJRu6sMtU5shjLxe1WioDWX/eAQJBAOtDIC4yjLVS42SNR6zrp0ntskYUGNT9n80znPbJjdZ5yymlPTQRdzR6n6UubjtQxEkYq22X6DTeWRW0EORXENkCQQCdoIq8bZ/I8lSakd2UaugM8IwBVSIsgGZ969BBRoZPTyIk5Vht1XJ2X9b2xxIoADyJZfd0sqNysQhULw5uZmUzAkBtfccrWQFdnl8QPCSAmQg5gvO2Y8IO1p8Z3IyP2sw1ZmekUTAD3KES/oLwWISa/ILt1hpqnglHGbhyPmSiMNc5AkBNRyH9UzldCQFVbmHVm7v8bAoXtSc17hVRcsT825iJVWCF+jKqVlTxl/cJsXtDRSpoqibxfYsIdaaBrzhCA81lAkEA6fDs2dWBXDVcnTREr71y6UMyKubxE0aGJMsoE/FTkOo8y0z/EDewZoVnK5qFSOPZUZoknCQiVP3kln15BZvWQg==";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfYQmI9qwot7MsIIcJ19ZxeDdajUByjxKNn5zkT8dTsrqOM1M/PY5Tt+lsNxzWQFplElN3de2LKMGG6Q3NQ9qHGWusTLVOW1cpafHcatDwIWV8MZ0E+SgCMgvIJbU3ZUOG3KZEgVkA9qiL93oMMKRKoAPo4LS4gSKQViHkAPKoBwIDAQAB";

		client = new RSAGatewayClient(serverURL, partnerId, publicKey, privateKey, Charset.forName("UTF-8"), new JSON(), new HttpURLClient());
	}

}
