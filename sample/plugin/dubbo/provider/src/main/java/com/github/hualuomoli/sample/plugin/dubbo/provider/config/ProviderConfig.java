package com.github.hualuomoli.sample.plugin.dubbo.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;

@Configuration
public class ProviderConfig {

  @Bean
  public ApplicationConfig applicationConfig() {
    return new ApplicationConfig("provider");
  }

  @Bean
  public RegistryConfig registryConfig() {
    return new RegistryConfig("zookeeper://127.0.0.1:2181");
  }

  @Bean
  public ProtocolConfig protocolConfig() {
    return new ProtocolConfig("dubbo", 20880);
  }

  @Bean
  public AnnotationBean annotationBean() {
    AnnotationBean bean = new AnnotationBean();
    bean.setPackage("com.github.hualuomoli.sample.plugin.dubbo.provider");

    return bean;
  }

}
