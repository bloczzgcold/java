package com.github.hualuomoli.sample.plugin.mq.sender;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.hualuomoli.config.BaseComponentConfig;
import com.github.hualuomoli.config.ConnectionConfig;
import com.github.hualuomoli.config.JmsTemplateConfig;
import com.github.hualuomoli.mq.sender.MessageSender.Type;
import com.github.hualuomoli.mq.sender.jms.JmsMessageSender;
import com.github.hualuomoli.util.ProjectConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseComponentConfig.class, ConnectionConfig.class, JmsTemplateConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class SenderTest {

  protected static final Logger logger = LoggerFactory.getLogger(SenderTest.class);

  @Resource(name = "jmsTemplate")
  private JmsTemplate jmsTemplate;

  @BeforeClass
  public static void beforeClass() {
    ProjectConfig.init("classpath*:configs/activemq.properties");
  }

  @Test
  public void testSendQueue() {

    JmsMessageSender sender = new JmsMessageSender();
    sender.setJmsTemplate(jmsTemplate);

    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    int count = (int) (Math.random() * 20) + 50;
    logger.info("timestamp={}", timestamp);
    logger.info("count={}", count);

    for (int i = 1; i <= count; i++) {
      sender.send("sample_queue", i + " " + timestamp + " 单点message");
    }

  }

  @Test
  public void testSendTopic() {
    JmsMessageSender sender = new JmsMessageSender();
    sender.setJmsTemplate(jmsTemplate);

    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    int count = (int) (Math.random() * 10) + 20;
    logger.info("timestamp={}", timestamp);
    logger.info("count={}", count);

    for (int i = 1; i <= count; i++) {
      sender.send("sample_topic", i + " " + timestamp + " 广播message", Type.TOPIC);
    }

  }

}
