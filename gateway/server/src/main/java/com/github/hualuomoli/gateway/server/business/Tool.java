package com.github.hualuomoli.gateway.server.business;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class Tool {

  private static final Logger logger = LoggerFactory.getLogger(Tool.class);

  // 功能map
  private final Map<String, List<Function>> functionMap = new HashMap<String, List<Function>>();

  private Parser parser;

  Tool(Parser parser) {
    this.parser = parser;
    this.init();
  }

  private void init() {
    List<Class<?>> clazzes = parser.dealers();
    for (Class<?> clazz : clazzes) {
      Method[] methods = clazz.getDeclaredMethods();
      for (Method method : methods) {
        String gatewayMethod = parser.loadGatewayMethod(clazz, method);
        if (gatewayMethod == null) {
          continue;
        }
        String version = parser.loadVersion(clazz, method);
        Function function = new Function(gatewayMethod, version, method, clazz);

        logger.info("load method " + gatewayMethod + ",version=" + version);

        List<Function> functions = functionMap.get(gatewayMethod);
        if (functions == null) {
          functions = new ArrayList<Function>();
          functionMap.put(gatewayMethod, functions);
        }
        functions.add(function);
      }
    }

  }

  /**
   * 获取处理方法
   * @param method 请求方法
   * @param version 请求版本
   * @return 功能
   */
  Function getFunction(String method, String version) {
    List<Function> functions = functionMap.get(method);
    if (functions == null || functions.size() == 0) {
      return null;
    }

    if (functions.size() == 1) {
      return functions.get(0);
    }

    boolean empty = version == null || version.trim().length() == 0;

    // check version
    // 1、support functions
    List<Function> list = new ArrayList<Function>();
    for (Function function : functions) {
      if (empty || parser.support(function.version, version)) {
        list.add(function);
      }
    }

    if (list == null || list.size() == 0) {
      return null;
    }

    // 倒序
    Collections.sort(list, new Comparator<Function>() {

      @Override
      public int compare(Function o1, Function o2) {
        return parser.compare(o2.version, o1.version);
      }
    });

    return list.get(0);
  }

  /**
   * 获取处理者
   * @param function 功能
   * @return 处理者
   */
  Object getDealer(Function function) {
    return parser.instance(function.clazz);
  }

}
