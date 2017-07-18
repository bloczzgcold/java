package com.github.hualuomoli.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.hualuomoli.util.ProjectConfig;

@Configuration(value = "com.github.hualuomoli.config.ConnectionConfig")
public class ConnectionConfig {

  @Bean(name = "receiveConnectionFactory1")
  public ConnectionFactory receiveConnectionFactory1() {
    String username = ProjectConfig.getString("username");
    String password = ProjectConfig.getString("password");
    String brokerURL = ProjectConfig.getString("activemq.brokerURL");
    return new ActiveMQConnectionFactory(username, password, brokerURL);
  }

  @Bean(name = "receiveConnectionFactory2")
  public ConnectionFactory receiveConnectionFactory2() {
    String username = ProjectConfig.getString("username");
    String password = ProjectConfig.getString("password");
    String brokerURL = ProjectConfig.getString("activemq.brokerURL");
    return new ActiveMQConnectionFactory(username, password, brokerURL);
  }

  @Bean(name = "receiveConnectionFactory3")
  public ConnectionFactory receiveConnectionFactory3() {
    String username = ProjectConfig.getString("username");
    String password = ProjectConfig.getString("password");
    String brokerURL = ProjectConfig.getString("activemq.brokerURL");
    return new ActiveMQConnectionFactory(username, password, brokerURL);

  }

}
