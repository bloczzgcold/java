package com.github.hualuomoli.gateway.client.android.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import com.github.hualuomoli.gateway.client.json.JSONParser;

/**
 * JSON转换器
 * @author lbq
 *
 */
public class Gson implements JSONParser {

	@Override
	public String toJsonString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (String.class.isAssignableFrom(obj.getClass())) {
			return (String) obj;
		}

		return Gson.newInstance().toJson(obj);
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
		return Gson.newInstance().fromJson(text, clazz);
	}

	@Override
	public <T> List<T> parseArray(String text, Class<T> clazz) {
		if (text == null || text.trim().length() == 0) {
			return null;
		}

		com.google.gson.Gson gson = Gson.newInstance();
		Type type = Gson.getType(List.class, clazz);
		return gson.fromJson(text, type);
	}

	private static ParameterizedType getType(final Type raw, final Class<?>... clazz) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return clazz;
			}

			public Type getOwnerType() {
				return null;
			}
		};
	}

	private static com.google.gson.Gson newInstance() {
		return new com.google.gson.Gson();
	}
}
