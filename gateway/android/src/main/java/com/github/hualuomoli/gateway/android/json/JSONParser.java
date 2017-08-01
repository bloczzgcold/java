package com.github.hualuomoli.gateway.android.json;

import java.util.List;

/**
 * JSON转换器
 */
public interface JSONParser {

  <T> T parseObject(String json, Class<T> clazz);

  <T> List<T> parseArray(String json, Class<T> clazz);

}
