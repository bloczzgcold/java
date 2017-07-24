package com.github.hualuomoli.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.config.gateway.GatewayServerConfig;

/**
 * Gateway
 */
@Configuration(value = "com.github.hualuomoli.config.GatewayConfig")
@Import(value = { GatewayServerConfig.class })
public class GatewayConfig {

}
