package com.github.hualuomoli.plugin.mq.dealer.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.plugin.mq.MessageDealer;

@Service(value = "com.github.hualuomoli.plugin.mq.dealer.queue.QueueMessageDealer1")
public class QueueMessageDealer1 implements MessageDealer {

	private static final Logger logger = LoggerFactory.getLogger(QueueMessageDealer1.class);

	@Override
	public void onMessage(String data) {
		logger.info("receive {}", data);
	}

}
