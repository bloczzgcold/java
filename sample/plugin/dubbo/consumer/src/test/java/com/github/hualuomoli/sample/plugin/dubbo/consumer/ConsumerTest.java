package com.github.hualuomoli.sample.plugin.dubbo.consumer;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.hualuomoli.sample.plugin.dubbo.api.entity.User;
import com.github.hualuomoli.sample.plugin.dubbo.api.service.MyDubboService;
import com.github.hualuomoli.sample.plugin.dubbo.api.service.UseCommonService;
import com.github.hualuomoli.sample.plugin.dubbo.consumer.config.BaseComponentConfig;
import com.github.hualuomoli.sample.plugin.dubbo.consumer.config.ConsumerConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseComponentConfig.class, ConsumerConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ConsumerTest {

  protected static final Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

  @Reference
  private UseCommonService useCommonService;
  @Reference
  private MyDubboService myDubboService;

  @Test
  public void test() throws IOException {

    logger.info("dubbo consumer started.");

    // 返回复杂数据
    User user = useCommonService.get("hualuomoli");
    logger.debug("user={}", user);

    // 参数复杂数据
    String username = useCommonService.create(user);
    logger.debug("username={}", username);

    // 多参数
    boolean success = useCommonService.login("hualuomoli", "admin");
    logger.debug("success={}", success);

    // 多返回
    Object[] results = useCommonService.result();
    logger.debug("size={}", results.length);
    for (Object result : results) {
      logger.debug("className={},data={}", result.getClass().getName(), result);
    }

    //
    String content = myDubboService.say("hello");
    logger.debug("content={}", content);

  }

}
