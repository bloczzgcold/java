package com.github.hualuomoli.config.mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 配置静态资源
 */
@Configuration(value = "com.github.hualuomoli.config.mvc.StaticResourceConfig")
@EnableWebMvc
public class StaticResourceConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    System.out.println("初始化静态文件处理器");

    registry.addResourceHandler("/static/**").addResourceLocations("/static/");
  }

}
