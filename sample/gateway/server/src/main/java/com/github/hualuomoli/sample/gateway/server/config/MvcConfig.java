package com.github.hualuomoli.sample.gateway.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.sample.gateway.server.config.mvc.MvcComponentConfig;

/**
 * mvc
 */
@Configuration
@Import(value = { MvcComponentConfig.class })
public class MvcConfig {

}
