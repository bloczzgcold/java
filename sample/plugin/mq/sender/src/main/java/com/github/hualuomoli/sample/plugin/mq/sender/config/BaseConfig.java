package com.github.hualuomoli.sample.plugin.mq.sender.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.sample.plugin.mq.sender.config.base.BaseComponentConfig;
import com.github.hualuomoli.sample.plugin.mq.sender.config.base.ConnectionConfig;
import com.github.hualuomoli.sample.plugin.mq.sender.config.base.JmsTemplateConfig;

/**
 * base
 */
@Configuration
@Import(value = { BaseComponentConfig.class //
    , ConnectionConfig.class //
    , JmsTemplateConfig.class })
public class BaseConfig {

}
