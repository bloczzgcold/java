package com.github.hualuomoli.gateway.server.business;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.hualuomoli.gateway.server.constants.NameEnum;
import com.github.hualuomoli.gateway.server.lang.InvalidParameterException;
import com.github.hualuomoli.gateway.server.lang.InvalidRequestParameterAnnotationException;
import com.github.hualuomoli.gateway.server.lang.NoMethodFoundException;
import com.github.hualuomoli.gateway.server.parser.JSONParser;
import com.github.hualuomoli.validate.util.ValidatorUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Spring处理
 * @author lbq
 *
 */
public class SpringBusinessHandler implements BusinessHandler, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(SpringBusinessHandler.class);

	private ApplicationContext context;
	private Class<? extends Annotation> apiAnnotation;
	private String packageName;

	private static Method apiAnnotaionValueMethod = null;

	private static final Map<String, List<Function>> functionMap = Maps.newHashMap();
	private static final String DEFAULT_VERSION = "0.0.0";

	public SpringBusinessHandler(Class<? extends Annotation> apiAnnotation, String packageName) {
		this.apiAnnotation = apiAnnotation;
		this.packageName = packageName;
	}

	@Override
	public String handle(String apiMethod, String bizContent, JSONParser jsonParser, HttpServletRequest req, HttpServletResponse res) throws Throwable {

		String version = this.getVersion(req);

		String realUrl = "/" + apiMethod.replaceAll("[.]", "/");

		Function function = this.getFunction(realUrl, version);

		if (function == null) {
			throw new NoMethodFoundException("没有执行方法" + apiMethod);
		}

		Object controller = context.getBean(function.clazz);
		Method method = function.method;

		Class<?>[] parameterTypes = method.getParameterTypes();
		Object[] params = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			Class<?> parameterType = parameterTypes[i];
			if (HttpServletRequest.class.isAssignableFrom(parameterType)) {
				params[i] = req;
			} else if (HttpServletResponse.class.isAssignableFrom(parameterType)) {
				params[i] = res;
			} else {
				// list
				if (List.class.isAssignableFrom(parameterType)) {
					try {
						Method m = parameterType.getMethod("get", new Class[] { int.class });
						Class<?> clazz = m.getReturnType();
						params[i] = jsonParser.parseArray(bizContent, clazz);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				} else if (packageName == null || parameterType.getName().startsWith(packageName)) {
					// 指定的包名
					Object object = jsonParser.parseObject(bizContent, parameterType);
					Set<String> errors = ValidatorUtils.valid(object);
					if (errors != null && errors.size() > 0) {
						throw new InvalidRequestParameterAnnotationException(errors);
					}
					params[i] = object;
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("can not inject data to {}", parameterType.getName());
					}
				}
			}
		}

		Object result = method.invoke(controller, params);

		if (result == null) {
			return null;
		}

		if (String.class.isAssignableFrom(method.getReturnType())) {
			return (String) result;
		}

		return jsonParser.toJsonString(result);
	}

	/**
	 * 获取请求的版本号
	 * @param req HTTP请求
	 * @return 版本号.如果没有设置版本号或者版本号为空,返回null
	 */
	private String getVersion(HttpServletRequest req) {
		String version = req.getHeader(NameEnum.API_VERSION.value());
		if (version == null || version.trim().length() == 0) {
			return null;
		}
		return version.trim();
	}

	/**
	 * 获取执行功能 
	 * @param methodName 请求方法名
	 * @param apiVersion 请求API版本号
	 * @return 执行功能,如果没找到返回null
	 * @throws InvalidParameterException 请求没有指定API版本号
	 */
	private Function getFunction(String realUrl, String apiVersion) throws InvalidParameterException {

		// 初始化
		init();

		List<Function> supportFunctions = Lists.newArrayList();
		List<Function> functions = functionMap.get(realUrl);

		if (functions == null || functions.size() == 0) {
			return null;
		}

		// 获取支持的function
		for (Function function : functions) {
			if (apiVersion == null) {
				supportFunctions.add(function);
			} else if (this.compare(function.version, apiVersion) <= 0) {
				supportFunctions.add(function);
			}
		}

		if (supportFunctions.size() == 0) {
			logger.warn("there is no api support realUrl={}, version={}", realUrl, apiVersion);
			return null;
		}

		// 正序排序
		Collections.sort(supportFunctions, new Comparator<Function>() {

			@Override
			public int compare(Function o1, Function o2) {
				return SpringBusinessHandler.this.compare(o1.version, o2.version);
			}
		});

		// 获取最后一个
		Function function = supportFunctions.get(supportFunctions.size() - 1);
		logger.debug("url request version is {}, use {}", apiVersion, function.version);

		return function;
	}

	/**
	 * 初始化
	 */
	private void init() {

		if (functionMap.size() > 0) {
			return;
		}

		synchronized (functionMap) {

			if (functionMap.size() > 0) {
				return;
			}

			Map<String, Object> controllerMap = context.getBeansWithAnnotation(RequestMapping.class);
			for (String controllerName : controllerMap.keySet()) {

				Object controller = controllerMap.get(controllerName);
				Class<? extends Object> controllerClazz = controller.getClass();
				RequestMapping requestMapping = controllerClazz.getAnnotation(RequestMapping.class);

				this.initController(controllerClazz, requestMapping.value());

			}
		}

	}

	/**
	 * 初始化类
	 * @param controllerClazz 	类
	 * @param controllerUrls 	请求URL
	 */
	private void initController(Class<?> controllerClazz, String[] controllerUrls) {
		if (controllerUrls == null || controllerUrls.length == 0) {
			return;
		}
		for (String controllerUrl : controllerUrls) {
			this.initController(controllerClazz, controllerUrl);
		}
	}

	/**
	 * 初始化类
	 * @param controllerClazz 	类
	 * @param controllerUrl 	请求URL
	 */
	private void initController(Class<?> controllerClazz, String controllerUrl) {
		Method[] methods = controllerClazz.getMethods();
		if (methods == null || methods.length == 0) {
			return;
		}

		for (Method method : methods) {

			// 共有方法
			if (!Modifier.isPublic(method.getModifiers())) {
				continue;
			}
			// 请求url注解
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
			if (requestMapping == null) {
				continue;
			}

			this.initMethod(controllerClazz, controllerUrl, method, requestMapping.value());
		}
	}

	/**
	 * 初始化方法
	 * @param controllerClazz　	类
	 * @param controllerUrl　	类的请求URL
	 * @param method　			方法
	 * @param methodUrls 		方法的URL
	 */
	private void initMethod(Class<?> controllerClazz, String controllerUrl, Method method, String[] methodUrls) {
		if (methodUrls == null) {
			return;
		}
		for (String methodUrl : methodUrls) {
			this.initMethod(controllerClazz, controllerUrl, method, methodUrl);
		}
	}

	/**
	 * 初始化方法
	 * @param controllerClazz　	类
	 * @param controllerUrl　	类的请求URL
	 * @param method　			方法
	 * @param methodUrl 		方法的URL
	 */
	private void initMethod(Class<?> controllerClazz, String controllerUrl, Method method, String methodUrl) {

		// /user/loadUserMessage
		String realUrl = "/" + controllerUrl + "/" + methodUrl + "/";
		realUrl = realUrl.replaceAll("//", "/");
		realUrl = realUrl.substring(0, realUrl.length() - 1);

		Function function = new Function();
		function.realUrl = realUrl;
		function.method = method;
		function.clazz = controllerClazz;
		function.version = this.getApiVersion(controllerClazz, method);

		if (logger.isDebugEnabled()) {
			logger.debug("init realUrl={},version={}", function.realUrl, function.version);
		}

		List<Function> functions = functionMap.get(realUrl);
		if (functions == null) {
			functions = Lists.newArrayList();
			functionMap.put(realUrl, functions);
		}

		functions.add(function);
	}

	/**
	 * 获取API版本号
	 * @param controllerClazz 类
	 * @param method 方法
	 * @return API版本号
	 */
	private String getApiVersion(Class<?> controllerClazz, Method method) {
		Annotation controllerApiVersion = controllerClazz.getAnnotation(apiAnnotation);
		Annotation methodApiVersion = method.getAnnotation(apiAnnotation);
		if (methodApiVersion != null) {
			return this.getApiVersion(methodApiVersion);
		}

		if (controllerApiVersion != null) {
			return this.getApiVersion(controllerApiVersion);
		}

		return DEFAULT_VERSION;

	}

	/**
	 * 获取注解的API版本号
	 * @param apiAnnotation 注解
	 * @return API版本号
	 */
	private String getApiVersion(Annotation apiAnnotation) {
		if (apiAnnotaionValueMethod == null) {
			try {
				apiAnnotaionValueMethod = apiAnnotation.getClass().getMethod("value");
			} catch (Exception e) {
				throw new RuntimeException("please add method String value().");
			}
		}

		try {
			return (String) apiAnnotaionValueMethod.invoke(apiAnnotation);
		} catch (Exception e) {
			throw new RuntimeException("please add method String value().");
		}

	}

	/**
	 * 比较两个版本
	 * @param version1 第一个版本
	 * @param version2 第二个版本
	 * @return 第一个与第二个版本的比较
	 */
	private int compare(String version1, String version2) {

		if (version1 == null && version2 == null) {
			return 0;
		}

		if (version1 == null) {
			return -1;
		}

		if (version2 == null) {
			return 1;
		}

		if (version1.equals(version2)) {
			return 0;
		}

		String[] array1 = version1.split("[.]");
		String[] array2 = version2.split("[.]");

		int len1 = array1.length;
		int len2 = array2.length;

		int len = len1 > len2 ? len2 : len1;
		for (int i = 0; i < len; i++) {
			int v1 = Integer.parseInt(array1[i]);
			int v2 = Integer.parseInt(array2[i]);

			if (v1 == v2) {
				continue;
			}

			return v1 > v2 ? 1 : -1;
		}

		return len1 > len2 ? 1 : -1;
	}

	// 功能
	private static final class Function {
		/** 请求的url路径 */
		private String realUrl;
		/** 请求的版本号 */
		private String version;
		/** 请求的方法 */
		private Method method;
		/** 请求的功能类 */
		private Class<?> clazz;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}

}
