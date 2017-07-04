package com.github.hualuomoli.sample.plugin.mq.receiver.config.base;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {

  @Bean(name = "receiveConnectionFactory1")
  public ConnectionFactory receiveConnectionFactory1() {
    String username = "admin";
    String password = "admin";
    String brokerURL = "failover:(tcp://127.0.0.1:61616)";
    return new ActiveMQConnectionFactory(username, password, brokerURL);
  }

  @Bean(name = "receiveConnectionFactory2")
  public ConnectionFactory receiveConnectionFactory2() {
    String username = "admin";
    String password = "admin";
    String brokerURL = "failover:(tcp://127.0.0.1:61616)";
    return new ActiveMQConnectionFactory(username, password, brokerURL);
  }

  @Bean(name = "receiveConnectionFactory3")
  public ConnectionFactory receiveConnectionFactory3() {
    String username = "admin";
    String password = "admin";
    String brokerURL = "failover:(tcp://127.0.0.1:61616)";
    return new ActiveMQConnectionFactory(username, password, brokerURL);

  }

}
