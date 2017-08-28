package com.github.hualuomoli.config;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.mq.receiver.listener.DefaultMessageListener;
import com.github.hualuomoli.mq.receiver.listener.DefaultTopicMessageListener;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic.TopicMessageDealer1;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic.TopicMessageDealer2;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic.TopicMessageDealer3;

@Configuration(value = "com.github.hualuomoli.config.TopicActiveMqListenerConfig")
@Import(value = { ConnectionConfig.class, BaseComponentConfig.class })
public class TopicActiveMqListenerConfig {

  @Resource(name = "receiveConnectionFactory1")
  private ConnectionFactory receiveConnectionFactory1;
  @Resource(name = "receiveConnectionFactory2")
  private ConnectionFactory receiveConnectionFactory2;
  @Resource(name = "receiveConnectionFactory3")
  private ConnectionFactory receiveConnectionFactory3;

  @Autowired
  private TopicMessageDealer1 topicMessageDealer1;
  @Autowired
  private TopicMessageDealer2 topicMessageDealer2;
  @Autowired
  private TopicMessageDealer3 topicMessageDealer3;

  @Bean
  public DefaultMessageListener topicActiveMqListener1() {
    DefaultMessageListener listener = new DefaultTopicMessageListener(receiveConnectionFactory1, topicMessageDealer1, "client1");
    return listener;
  }

  @Bean
  public DefaultMessageListener topicActiveMqListener2() {
    DefaultMessageListener listener = new DefaultTopicMessageListener(receiveConnectionFactory2, topicMessageDealer2, "client2");
    return listener;
  }

  @Bean
  public DefaultMessageListener topicActiveMqListener3() {
    DefaultMessageListener listener = new DefaultTopicMessageListener(receiveConnectionFactory3, topicMessageDealer3, "client3");
    return listener;
  }

}
