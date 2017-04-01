package com.github.hualuomoli.test.framework.config.base;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

/**
 * Mybatis扫描配置,需要与mybatis的其他配置分开
 * @author lbq
 *
 */
@Configuration
public class MybatisScannerConfig {

	private static final Logger logger = LoggerFactory.getLogger(MybatisScannerConfig.class);

	@Bean
	public MapperScannerConfigurer loadMapperScannerConfigurer() throws ClassNotFoundException {

		logger.info("初始化mybatis接口mapper");

		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage("com.github.hualuomoli");
		mapperScannerConfigurer.setAnnotationClass(Repository.class);

		return mapperScannerConfigurer;
	}

}
