package com.github.hualuomoli.config.base;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.github.hualuomoli.framework.plugin.mybatis.dialect.db.MySQLDialect;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import com.github.hualuomoli.util.ProjectConfig;
import com.google.common.collect.Lists;

/**
 * Mybatis配置
 */
@Configuration(value = "com.github.hualuomoli.config.base.MybatisConfig")
@Import({ DataSourceConfig.class })
public class MybatisConfig {

  private static List<TypeHandler<?>> typeHandlers = Lists.newArrayList();

  public static void addTypeHandler(TypeHandler<?> typeHandler) {
    typeHandlers.add(typeHandler);
  }

  @Resource(name = "dataSource1")
  private DataSource dataSource1;
  @Resource(name = "dataSource2")
  private DataSource dataSource2;

  @Bean(name = "sqlSessionFactory1")
  public SqlSessionFactoryBean loadSqlSessionFactoryBean() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

    System.out.println("初始化sqlSessionFactory");

    ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());

    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource1);
    sqlSessionFactoryBean.setMapperLocations(resolver.getResources(ProjectConfig.getString("mybatis.1.mapperLocations")));

    // 自定义处理解析类
    sqlSessionFactoryBean.setTypeHandlers(typeHandlers.toArray(new TypeHandler<?>[] {}));

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

  @Bean(name = "sqlSessionFactory2")
  public SqlSessionFactoryBean loadSqlSessionFactoryBean2() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {

    System.out.println("初始化sqlSessionFactory");

    ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader());

    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource2);
    sqlSessionFactoryBean.setMapperLocations(resolver.getResources(ProjectConfig.getString("mybatis.2.mapperLocations")));

    // 自定义处理解析类
    sqlSessionFactoryBean.setTypeHandlers(typeHandlers.toArray(new TypeHandler<?>[] {}));

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

}
