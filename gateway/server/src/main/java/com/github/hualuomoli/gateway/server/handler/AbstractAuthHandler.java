package com.github.hualuomoli.gateway.server.handler;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.gateway.server.util.Utils;

/**
 * 网关认证执行者
 * @author lbq
 *
 */
public abstract class AbstractAuthHandler implements AuthHandler {

	/**
	 * 转换request为实体类
	 * @param req 请求
	 * @param jsonParser JSON转换器
	 * @param clazz 转换后的实体类类型
	 * @return 实体类
	 */
	protected <T> T parse(ServletRequest req, JSONParser jsonParser, Class<T> clazz) {
		Map<String, String> map = new HashMap<String, String>();

		Enumeration<String> names = req.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = req.getParameter(name);
			if (value == null || value.trim().length() == 0) {
				continue;
			}
			map.put(name, value);
		}

		return jsonParser.parseObject(jsonParser.toJsonString(map), clazz);
	}

	/**
	 * 验证实体类是否合法
	 * @param obj 实体类
	 */
	protected void check(Object obj) {
		if (obj == null) {
			return;
		}

		Class<?> clazz = obj.getClass();
		List<Field> fields = Utils.getFields(clazz);
		for (Field field : fields) {
			String name = field.getName();
			String value = null;

			try {
				String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
				value = (String) clazz.getMethod(methodName).invoke(obj);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

			// not blank
			NotBlank notBlank = field.getAnnotation(NotBlank.class);
			if (notBlank != null) {
				if (value == null || value.trim().length() == 0) {
					throw new IllegalArgumentException(notBlank.message());
				}
			}

			// pattern
			Pattern pattern = field.getAnnotation(Pattern.class);
			if (pattern != null) {
				if (!value.matches(pattern.regex())) {
					throw new IllegalArgumentException(pattern.message());
				}
			}

		}
	}

}
