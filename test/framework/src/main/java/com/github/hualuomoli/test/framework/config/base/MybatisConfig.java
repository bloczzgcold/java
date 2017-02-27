package com.github.hualuomoli.test.framework.config.base;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.github.hualuomoli.framework.plugin.mybatis.dialect.db.MySQLDialect;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;

/**
 * Mybatis配置
 * @author lbq
 *
 */
@Configuration
@Import({ DataSourceConfig.class })
public class MybatisConfig {

	private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);

	@Resource(name = "dataSource")
	DataSource dataSource;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactoryBean loadSqlSessionFactoryBean() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		logger.info("初始化sqlSessionFactory");

		ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());

		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mappers/**/*Mapper.xml"));

		// 自定义处理解析类
		// sqlSessionFactoryBean.setTypeHandlers(typeHandlers);

		// 分页插件
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// 设置属性
		Properties properties = new Properties();
		paginationInterceptor.setProperties(properties);
		// 设置方言
		paginationInterceptor.setDialect(new MySQLDialect());
		// 添加插件
		sqlSessionFactoryBean.setPlugins(new Interceptor[] { paginationInterceptor });

		return sqlSessionFactoryBean;
	}

	@Bean(name = "transactionManager")
	public DataSourceTransactionManager loadDataSourceTransactionManager() {

		logger.info("instance transactionManager.");

		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);

		return transactionManager;
	}

}
