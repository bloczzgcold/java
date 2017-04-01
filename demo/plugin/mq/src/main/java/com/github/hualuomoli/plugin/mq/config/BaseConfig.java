package com.github.hualuomoli.plugin.mq.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.plugin.mq.config.base.BaseComponentConfig;
import com.github.hualuomoli.plugin.mq.config.base.ConnectionConfig;
import com.github.hualuomoli.plugin.mq.config.base.JmsTemplateConfig;
import com.github.hualuomoli.plugin.mq.config.base.QueueActiveMqListenerConfig;
import com.github.hualuomoli.plugin.mq.config.base.TopicActiveMqListenerConfig;

/**
 * 切面
 * @author lbq
 *
 */
@Configuration
@Import(value = { BaseComponentConfig.class //
		, ConnectionConfig.class //
		, JmsTemplateConfig.class //
		, QueueActiveMqListenerConfig.class //
		, TopicActiveMqListenerConfig.class })
public class BaseConfig {

}
