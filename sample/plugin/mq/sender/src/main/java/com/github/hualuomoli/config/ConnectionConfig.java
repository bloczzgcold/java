package com.github.hualuomoli.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;

import com.github.hualuomoli.util.ProjectConfig;

@Configuration
public class ConnectionConfig {

  @Bean(name = "sendConnectionFactory")
  public ConnectionFactory sendConnectionFactory() {
    String username = ProjectConfig.getString("activemq.username");
    String password = ProjectConfig.getString("activemq.password");
    String brokerURL = ProjectConfig.getString("activemq.brokerURL");
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);

    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
    cachingConnectionFactory.setSessionCacheSize(ProjectConfig.getInteger("activemq.cache.size"));

    return cachingConnectionFactory;
  }

}
