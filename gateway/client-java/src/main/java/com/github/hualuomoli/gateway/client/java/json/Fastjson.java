package com.github.hualuomoli.gateway.client.java.json;

import java.util.List;

import com.github.hualuomoli.gateway.client.json.JSONParser;

/**
 * JSON转换器
 * @author lbq
 *
 */
public class Fastjson implements JSONParser {

	@Override
	public String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (String.class.isAssignableFrom(obj.getClass())) {
			return (String) obj;
		}
		return com.alibaba.fastjson.JSON.toJSONString(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T parseObject(String text, Class<T> clazz) {
		if (text == null || text.trim().length() == 0) {
			return null;
		}
		if (String.class.isAssignableFrom(clazz)) {
			return (T) text;
		}
		return com.alibaba.fastjson.JSON.parseObject(text, clazz);
	}

	@Override
	public <T> List<T> parseArray(String text, Class<T> clazz) {
		if (text == null || text.trim().length() == 0) {
			return null;
		}

		return com.alibaba.fastjson.JSON.parseArray(text, clazz);
	}
}
