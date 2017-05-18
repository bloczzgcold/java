package com.github.hualuomoli.gateway.server.loader;

import java.util.Map;

/**
 * 合作伙伴加载器
 * @author lbq
 *
 */
public interface PartnerLoader {

	/**
	 * 加载合作伙伴,如果合作伙伴未找到返回null
	 * @param partnerId 合作伙伴ID
	 * @return 合作伙伴或null
	 */
	Partner load(String partnerId);

	/** 合作伙伴信息 */
	public static final class Partner {
		/** 合作伙伴ID */
		private String partnerId;
		/** 合作伙伴名称 */
		private String name;
		/** 配置信息 */
		private Map<Key, String> configs;

		public Partner(String partnerId, String name, Map<Key, String> configs) {
			this.partnerId = partnerId;
			this.name = name;
			this.configs = configs;
		}

		public String getPartnerId() {
			return partnerId;
		}

		public String getName() {
			return name;
		}
		
		public String getConfigValue(Key key){
			if(configs == null || configs.isEmpty()){
				return null;
			}
			String value = configs.get(key);
			return value;
		}

	}

	/** 配置参数的key名称 */
	public static enum Key {
		/** RSA签名(公钥) */
		SIGNATURE_RSA_PUBLIC_KEY,
		/** MD5签名 */
		SIGNATURE_MD5,
		/** AES加密 */
		ENCRYPTION_AES;

	}

}
