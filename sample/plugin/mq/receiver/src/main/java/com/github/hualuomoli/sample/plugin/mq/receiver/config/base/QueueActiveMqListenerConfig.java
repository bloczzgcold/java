package com.github.hualuomoli.sample.plugin.mq.receiver.config.base;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.plugin.mq.listener.DefaultMessageListener;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue.QueueMessageDealer1;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue.QueueMessageDealer2;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue.QueueMessageDealer3;

@Configuration
@Import(value = { ConnectionConfig.class, BaseComponentConfig.class })
public class QueueActiveMqListenerConfig {

  private String destinationName = "core_queue";

  @Resource(name = "receiveConnectionFactory1")
  private ConnectionFactory receiveConnectionFactory1;
  @Resource(name = "receiveConnectionFactory2")
  private ConnectionFactory receiveConnectionFactory2;
  @Resource(name = "receiveConnectionFactory3")
  private ConnectionFactory receiveConnectionFactory3;

  @Autowired
  private QueueMessageDealer1 queueMessageDealer1;
  @Autowired
  private QueueMessageDealer2 queueMessageDealer2;
  @Autowired
  private QueueMessageDealer3 queueMessageDealer3;

  @Bean
  public DefaultMessageListener queueActiveMqListener1() {
    DefaultMessageListener listener = new DefaultMessageListener();
    listener.setDestinationName(destinationName);
    listener.setConnectionFactory(receiveConnectionFactory1);
    listener.setMessageDealer(queueMessageDealer1);
    return listener;
  }

  @Bean
  public DefaultMessageListener queueActiveMqListener2() {
    DefaultMessageListener listener = new DefaultMessageListener();
    listener.setDestinationName(destinationName);
    listener.setConnectionFactory(receiveConnectionFactory2);
    listener.setMessageDealer(queueMessageDealer2);
    return listener;
  }

  @Bean
  public DefaultMessageListener queueActiveMqListener3() {
    DefaultMessageListener listener = new DefaultMessageListener();
    listener.setDestinationName(destinationName);
    listener.setConnectionFactory(receiveConnectionFactory3);
    listener.setMessageDealer(queueMessageDealer3);
    return listener;
  }

}
