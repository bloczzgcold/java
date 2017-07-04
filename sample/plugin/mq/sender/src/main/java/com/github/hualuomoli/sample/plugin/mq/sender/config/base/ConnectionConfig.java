package com.github.hualuomoli.sample.plugin.mq.sender.config.base;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
public class ConnectionConfig {

  @Bean(name = "sendConnectionFactory")
  public ConnectionFactory sendConnectionFactory() {
    String username = "admin";
    String password = "admin";
    String brokerURL = "failover:(tcp://127.0.0.1:61616)";
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);

    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
    cachingConnectionFactory.setSessionCacheSize(100);

    return cachingConnectionFactory;
  }

}
