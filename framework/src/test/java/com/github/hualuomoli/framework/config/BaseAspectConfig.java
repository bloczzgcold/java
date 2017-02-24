package com.github.hualuomoli.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 切面
 * @author lbq
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BaseAspectConfig {

}
