package com.github.hualuomoli.sample.framework.creator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.hualuomoli.config.BaseConfig;
import com.github.hualuomoli.creator.reverse.component.parser.Parser.Resolver;
import com.github.hualuomoli.creator.reverse.service.ReversService;
import com.github.hualuomoli.sample.framework.ServiceTest;
import com.github.hualuomoli.sample.framework.enums.StateEnum;
import com.github.hualuomoli.sample.framework.enums.StatusEnum;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class EntityCratorTest extends ServiceTest {

  private static String outputPath = null;
  private String packageName = "com.github.hualuomoli.sample.framework";

  @Autowired
  private ReversService reversService;

  @Before
  public void before() {
    outputPath = ServiceTest.class.getResource("/").getPath();
    outputPath = outputPath.substring(0, outputPath.lastIndexOf("/target/"));
    logger.info("outputPath={}", outputPath);
  }

  @Test
  public void testCreate() {
    reversService.create(outputPath, packageName, "hualuomoli", "t_user", "User", new Resolver() {

      @Override
      public Class<?> resolverJavaType(Class<?> javaType, String javaName, String dbName) {
        Class<?> result = null;
        switch (dbName) {
        case "state":
          result = StateEnum.class;
          break;
        case "status":
          result = StatusEnum.class;
          break;
        default:
          result = javaType;
          break;
        }
        return result;
      }
    });

    logger.info("revers created.");
  }

}
