package com.github.hualuomoli.gateway.client.json;

import java.util.List;

public class JSONAdaptor implements JSON {

	@Override
	public String toJSONString(Object object) {
		return com.alibaba.fastjson.JSON.toJSONString(object);
	}

	@Override
	public <T> T parseObject(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseObject(text, clazz);
	}

	@Override
	public <T> List<T> parseArray(String text, Class<T> clazz) {
		return com.alibaba.fastjson.JSON.parseArray(text, clazz);
	}

}
