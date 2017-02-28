package com.github.hualuomoli.plugin.mq.dealer.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.plugin.mq.MessageDealer;

@Service(value = "com.github.hualuomoli.plugin.mq.dealer.queue.TopicMessageDealer2")
public class TopicMessageDealer2 implements MessageDealer {

	private static final Logger logger = LoggerFactory.getLogger(TopicMessageDealer2.class);

	@Override
	public void onMessage(String data) {
		logger.info("receive {}", data);
	}

}
