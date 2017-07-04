package com.github.hualuomoli.sample.gateway.server.config.mvc;

import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置请求/响应消息转换
 */
@Configuration
@EnableWebMvc
public class ConvertConfig extends WebMvcConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(ConvertConfig.class);

  // 配置消息转换器
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    logger.info("初始化mvc 消息转换器");

    converters.add(stringConvert());
  }

  // 字符串转换
  @Bean
  public StringHttpMessageConverter stringConvert() {

    logger.info("初始化mvc 字符串消息转换器");

    return new StringHttpMessageConverter(Charset.forName("UTF-8"));
  }

}
