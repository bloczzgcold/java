package com.github.hualuomoli.sample.framework.initializer;

import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import com.github.hualuomoli.sample.framework.biz.ProjectConfig;
import com.github.hualuomoli.tool.util.EnvUtils;
import com.github.hualuomoli.tool.util.PropertyUtils;

/**
 * 配置
 */
@Order(1)
public class ConfigureInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    System.setProperty("environment", EnvUtils.Env.TEST.name());
    EnvUtils.init("environment");

    // 设置log4j
    String log4jFilename = "logs/log4j.properties";
    Properties prop = PropertyUtils.loadFirst(EnvUtils.parse(log4jFilename));
    PropertyConfigurator.configure(prop);

    // 初始化配置文件
    ProjectConfig.init("configs/jdbc.properties", "configs/config.properties");
  }

}
