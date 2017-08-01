package com.github.hualuomoli.gateway.android.json;

import java.lang.reflect.Type;

/**
 * JSON转换器
 */
public interface JSONParser {

  <T> T parseObject(String json, Type type);

}
