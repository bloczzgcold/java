package com.github.hualuomoli.config;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.mq.receiver.TopicMessageDealer;
import com.github.hualuomoli.mq.receiver.listener.DefaultMessageListener;
import com.github.hualuomoli.mq.receiver.listener.DefaultTopicMessageListener;

@Configuration(value = "com.github.hualuomoli.config.TopicActiveMqListenerConfig")
@Import(value = { ConnectionConfig.class, BaseComponentConfig.class })
public class TopicActiveMqListenerConfig {

  @Resource(name = "receiveConnectionFactory")
  private ConnectionFactory receiveConnectionFactory;

  @Autowired
  private TopicMessageDealer topicMessageDealer;

  @Bean
  public DefaultMessageListener topicActiveMqListener1() {
    DefaultMessageListener listener = new DefaultTopicMessageListener(receiveConnectionFactory, topicMessageDealer);
    return listener;
  }

}
