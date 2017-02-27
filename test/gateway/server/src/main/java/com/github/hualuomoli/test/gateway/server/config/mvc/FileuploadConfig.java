package com.github.hualuomoli.test.gateway.server.config.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * 配置HTTP文件上传
 * @author lbq
 *
 */
@Configuration
public class FileuploadConfig {

	private static final Logger logger = LoggerFactory.getLogger(FileuploadConfig.class);

	// 文件上传
	@Bean
	public MultipartResolver multipartResolver() {
		logger.info("初始化文件上传 multipartResolver");

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(10 * 1024 * 1024);
		multipartResolver.setDefaultEncoding("UTF-8");
		return multipartResolver;
	}

}
