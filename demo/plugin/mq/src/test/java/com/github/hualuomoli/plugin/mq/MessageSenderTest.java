package com.github.hualuomoli.plugin.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;

import com.github.hualuomoli.plugin.mq.MessageSender.Type;
import com.github.hualuomoli.plugin.mq.jms.JmsMessageSender;

public class MessageSenderTest extends ServiceTest {

	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;

	@AfterClass
	public static void afterClass() throws InterruptedException {
		Thread.sleep(1000 * 2);
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
			sender.send("ln_core_queue", i + " " + timestamp + " 单点message");
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
			sender.send("ln_core_topic", i + " " + timestamp + " 广播message", Type.TOPIC);
		}

	}

}
