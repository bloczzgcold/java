package com.github.hualuomoli.sample.framework.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源
 */
@Configuration
@EnableWebMvc
public class StaticResourceConfig extends WebMvcConfigurerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(StaticResourceConfig.class);

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    logger.debug("初始化静态文件处理器");

    registry.addResourceHandler("/static/**").addResourceLocations("/static/");
  }

}
