package com.github.hualuomoli.sample.plugin.mq.receiver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.sample.plugin.mq.receiver.config.base.BaseComponentConfig;
import com.github.hualuomoli.sample.plugin.mq.receiver.config.base.ConnectionConfig;
import com.github.hualuomoli.sample.plugin.mq.receiver.config.base.QueueActiveMqListenerConfig;
import com.github.hualuomoli.sample.plugin.mq.receiver.config.base.TopicActiveMqListenerConfig;

/**
 * base
 */
@Configuration
@Import(value = { BaseComponentConfig.class //
    , ConnectionConfig.class //
    , QueueActiveMqListenerConfig.class //
    , TopicActiveMqListenerConfig.class })
public class BaseConfig {

}
