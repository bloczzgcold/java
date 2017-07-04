package com.github.hualuomoli.sample.plugin.mq.sender;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.hualuomoli.plugin.mq.MessageSender.Type;
import com.github.hualuomoli.plugin.mq.jms.JmsMessageSender;
import com.github.hualuomoli.sample.plugin.mq.sender.config.BaseConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class SenderTest {

  protected static final Logger logger = LoggerFactory.getLogger(SenderTest.class);

  @Resource(name = "jmsTemplate")
  private JmsTemplate jmsTemplate;

  @Test
  public void testSendQueue() {

    JmsMessageSender sender = new JmsMessageSender();
    sender.setJmsTemplate(jmsTemplate);

    String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    int count = (int) (Math.random() * 20) + 50;
    logger.info("timestamp={}", timestamp);
    logger.info("count={}", count);

    for (int i = 1; i <= count; i++) {
      sender.send("core_queue", i + " " + timestamp + " 单点message");
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
      sender.send("core_topic", i + " " + timestamp + " 广播message", Type.TOPIC);
    }

  }

}
