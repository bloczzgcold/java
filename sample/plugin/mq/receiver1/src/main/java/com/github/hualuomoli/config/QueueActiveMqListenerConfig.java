package com.github.hualuomoli.config;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.mq.receiver.listener.DefaultMessageListener;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue.QueueMessageDealer;

@Configuration(value = "com.github.hualuomoli.config.QueueActiveMqListenerConfig")
@Import(value = { ConnectionConfig.class, BaseComponentConfig.class })
public class QueueActiveMqListenerConfig {

  @Resource(name = "receiveConnectionFactory")
  private ConnectionFactory receiveConnectionFactory;

  @Autowired
  private QueueMessageDealer queueMessageDealer;

  @Bean
  public DefaultMessageListener queueActiveMqListener1() {
    DefaultMessageListener listener = new DefaultMessageListener(receiveConnectionFactory, queueMessageDealer);
    listener.setConcurrentConsumers(3); // 同时启动3个Listener实例来消费消息。
    listener.setMaxConcurrentConsumers(5); // 最大启动5个Listener实例来消费消息。
    //    listener.setConcurrency("5-20"); // 最小并发数是5，最大并发数为20
    return listener;
  }

}
