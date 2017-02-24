package com.github.hualuomoli.gateway.server.business;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hualuomoli.gateway.server.parser.JSONParser;

/**
 * 业务处理者
 * @author lbq
 *
 */
public interface BusinessHandler {

	/**
	 * 处理业务数据
	 * 
	 * @param methodName 	业务请求方法
	 * @param bizContent 	请求业务数据
	 * @param jsonParser 	JSON转换器
	 * @param req 			HTTP请求
	 * @param res 			HTTP响应
	 * @return 业务处理结果
	 * @throws Throwable 业务执行过程中出现的错误
	 */
	String handle(String methodName, String bizContent, JSONParser jsonParser, HttpServletRequest req, HttpServletResponse res) throws Throwable;

}
