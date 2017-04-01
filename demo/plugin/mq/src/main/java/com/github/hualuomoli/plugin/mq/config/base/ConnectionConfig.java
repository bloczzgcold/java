package com.github.hualuomoli.plugin.mq.config.base;

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
		String brokerURL = "failover:(tcp://activemq.hualuomoli.github.com:61616)";
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(username, password, brokerURL);

		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setTargetConnectionFactory(activeMQConnectionFactory);
		cachingConnectionFactory.setSessionCacheSize(100);

		return cachingConnectionFactory;
	}

	@Bean(name = "receiveConnectionFactory1")
	public ConnectionFactory receiveConnectionFactory1() {
		String username = "admin";
		String password = "admin";
		String brokerURL = "failover:(tcp://activemq.hualuomoli.github.com:61616)";
		return new ActiveMQConnectionFactory(username, password, brokerURL);
	}

	@Bean(name = "receiveConnectionFactory2")
	public ConnectionFactory receiveConnectionFactory2() {
		String username = "admin";
		String password = "admin";
		String brokerURL = "failover:(tcp://activemq.hualuomoli.github.com:61616)";
		return new ActiveMQConnectionFactory(username, password, brokerURL);
	}

	@Bean(name = "receiveConnectionFactory3")
	public ConnectionFactory receiveConnectionFactory3() {
		String username = "admin";
		String password = "admin";
		String brokerURL = "failover:(tcp://activemq.hualuomoli.github.com:61616)";
		return new ActiveMQConnectionFactory(username, password, brokerURL);

	}

}
