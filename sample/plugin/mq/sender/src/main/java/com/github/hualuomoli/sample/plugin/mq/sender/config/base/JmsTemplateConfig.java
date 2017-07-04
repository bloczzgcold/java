package com.github.hualuomoli.sample.plugin.mq.sender.config.base;

import javax.annotation.Resource;
import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@Import(value = { ConnectionConfig.class })
public class JmsTemplateConfig {

  @Resource(name = "sendConnectionFactory")
  private ConnectionFactory sendConnectionFactory;

  @Bean
  public JmsTemplate jmsTemplate() {
    JmsTemplate jmsTemplate = new JmsTemplate();
    jmsTemplate.setConnectionFactory(sendConnectionFactory);
    return jmsTemplate;
  }

}
