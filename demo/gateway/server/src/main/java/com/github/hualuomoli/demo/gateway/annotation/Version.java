package com.github.hualuomoli.demo.gateway.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求API版本号
 * @author lbq
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Version {

	/**
	 * 版本号
	 * @return 当前API的版本号,最小0.0.0
	 */
	String value();

}
