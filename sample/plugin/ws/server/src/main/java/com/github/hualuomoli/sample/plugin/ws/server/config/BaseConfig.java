package com.github.hualuomoli.sample.plugin.ws.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.sample.plugin.ws.server.config.base.BaseComponentConfig;

/**
 * base
 */
@Configuration
@Import(value = { BaseComponentConfig.class })
public class BaseConfig {

}
