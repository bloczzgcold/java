package com.github.hualuomoli.demo.gateway.server.biz.entity;

public class Partner {

	private String id;
	/** 合作伙伴ID */
	private String partnerId;
	/** 合作伙伴名称 */
	private String name;
	/** 配置信息 */
	private String configs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConfigs() {
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
	}

}
