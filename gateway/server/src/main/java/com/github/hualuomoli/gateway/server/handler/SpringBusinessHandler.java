package com.github.hualuomoli.gateway.server.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.hualuomoli.gateway.server.parser.JSONParser;

/**
 * 业务处理者
 * @author lbq
 *
 */
public class SpringBusinessHandler implements BusinessHandler, ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(SpringBusinessHandler.class);

	private static final Map<String, List<Function>> functionMap = new HashMap<String, List<Function>>();

	private ApplicationContext context;

	// 属性
	private Class<? extends Annotation> apiAnnotation;
	private String packageName;

	public SpringBusinessHandler(Class<? extends Annotation> apiAnnotation, String packageName) {
		super();
		this.apiAnnotation = apiAnnotation;
		this.packageName = packageName;
	}

	@SuppressWarnings("restriction")
	@Override
	public String handle(HttpServletRequest req, HttpServletResponse res//
	, String partnerId, String apiMethod, String bizContent, JSONParser jsonParser//
	, List<HandlerInterceptor> interceptors) throws NoMethodFoundException, NoAuthorityException, Throwable {
		String version = this.getVersion(req);

		String apiUrl = "/" + apiMethod.replaceAll("[.]", "/");

		logger.debug("apiMethod={},apiUrl={}", apiMethod, apiUrl);

		Function function = this.getFunction(apiUrl, version);

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
				if (bizContent == null || bizContent.trim().length() == 0) {
					continue;
				}

				// list
				if (List.class.isAssignableFrom(parameterType)) {

					sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl genericParameterTypes = (sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) method
							.getGenericParameterTypes()[i];
					Class<?> clazz = (Class<?>) genericParameterTypes.getActualTypeArguments()[0];

					params[i] = jsonParser.parseArray(bizContent, clazz);
				} else if (packageName == null || parameterType.getName().startsWith(packageName)) {
					// 指定的包名
					Object object = jsonParser.parseObject(bizContent, parameterType);
					params[i] = object;
				} else {
					if (logger.isWarnEnabled()) {
						logger.warn("can not inject data to {}", parameterType.getName());
					}
				}
			}
		}

		Object result;
		Throwable t = null;
		try {

			// 业务调用前执行
			for (HandlerInterceptor interceptor : interceptors) {
				interceptor.preHandle(req, res, partnerId, apiMethod, method,controller, params);
			}

			// 执行业务操作
			result = method.invoke(controller, params);

			// 业务调用后执行
			for (HandlerInterceptor interceptor : interceptors) {
				interceptor.postHandle(req, res);
			}

		} catch (InvocationTargetException e) {
			t = e.getTargetException();
			throw t;
		} finally {
			// 完成调用
			for (HandlerInterceptor interceptor : interceptors) {
				interceptor.afterCompletion(req, res, t);
			}
		}

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
		String version = req.getHeader("apiVersion");
		if (version == null || version.trim().length() == 0) {
			return null;
		}
		return version.trim();
	}

	/**
	 * 获取执行功能 
	 * @param realUrl 请求方法名
	 * @param apiVersion 请求API版本号
	 * @return 执行功能,如果没找到返回null
	 */
	private Function getFunction(String realUrl, String apiVersion) {

		// 初始化
		init();

		List<Function> supportFunctions = new ArrayList<Function>();
		List<Function> functions = functionMap.get(realUrl);

		if (functions == null || functions.size() == 0) {
			return null;
		}

		// 获取支持的function
		for (Function function : functions) {
			if (apiVersion == null || function.version == null) {
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

			if (context == null) {
				throw new RuntimeException("please instance bean SpringBusinessHandler.");
			}

			Tool tool = new Tool(functionMap, apiAnnotation);

			Map<String, Object> controllerMap = context.getBeansWithAnnotation(RequestMapping.class);
			for (String controllerName : controllerMap.keySet()) {

				Object controller = controllerMap.get(controllerName);
				Class<? extends Object> controllerClazz = controller.getClass();
				RequestMapping requestMapping = controllerClazz.getAnnotation(RequestMapping.class);

				tool.initController(controllerClazz, requestMapping.value());
			}
		}

	}

	// 工具
	class Tool {

		private Map<String, List<Function>> functionMap;
		private Class<? extends Annotation> apiAnnotation;

		private Method apiAnnotaionValueMethod = null;

		public Tool(Map<String, List<Function>> functionMap, Class<? extends Annotation> apiAnnotation) {
			super();
			this.functionMap = functionMap;
			this.apiAnnotation = apiAnnotation;
		}

		/**
		 * 初始化类
		 * @param controllerClazz 	类
		 * @param controllerUrls 	请求URL
		 */
		void initController(Class<?> controllerClazz, String[] controllerUrls) {
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
		void initController(Class<?> controllerClazz, String controllerUrl) {
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
		void initMethod(Class<?> controllerClazz, String controllerUrl, Method method, String[] methodUrls) {
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
		void initMethod(Class<?> controllerClazz, String controllerUrl, Method method, String methodUrl) {

			// /user/loadUserMessage
			String apiUrl = "/" + controllerUrl + "/" + methodUrl + "/";
			apiUrl = apiUrl.replaceAll("//", "/");
			apiUrl = apiUrl.substring(0, apiUrl.length() - 1);

			String apiMethod = apiUrl.substring(1).replaceAll("/", ".");

			Function function = new Function();
			function.apiUrl = apiUrl;
			function.apiMethod = apiMethod;
			function.apiRegex = this.getApiUrlRegex(apiUrl);
			function.method = method;
			function.clazz = controllerClazz;
			function.version = this.getApiVersion(controllerClazz, method);

			logger.debug("init function {}", function);

			List<Function> functions = functionMap.get(apiUrl);
			if (functions == null) {
				functions = new ArrayList<Function>();
				functionMap.put(apiUrl, functions);
			}

			functions.add(function);
		}

		/**
		 * 获取API的请求URL的正则表达式
		 * @param apiUrl API的请求URL
		 * @return 请求的url正则表达式
		 */
		private String getApiUrlRegex(String apiUrl) {
			// TODO
			// 可扩展支持模糊匹配
			return apiUrl;
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

			return null;
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

	}

	// 功能
	private static final class Function {
		/** 请求的url路径 */
		private String apiUrl;
		/** 请求的方法 */
		private String apiMethod;
		/** 请求url正则表达式 */
		private String apiRegex;
		/** 请求的版本号 */
		private String version;
		/** 请求的方法 */
		private Method method;
		/** 请求的功能类 */
		private Class<?> clazz;

		@Override
		public String toString() {
			return "Function [apiUrl=" + apiUrl + ", apiMethod=" + apiMethod + ", apiRegex=" + apiRegex + ", version=" + version + "]";
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
	}
}
