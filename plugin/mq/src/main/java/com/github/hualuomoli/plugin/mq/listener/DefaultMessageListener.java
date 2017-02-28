package com.github.hualuomoli.plugin.mq.listener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.util.ErrorHandler;

import com.github.hualuomoli.plugin.mq.MessageDealer;
import com.github.hualuomoli.plugin.mq.lang.MessageQueueException;

/**
 * 默认消息监听器
 * @author lbq
 *
 */
public class DefaultMessageListener extends DefaultMessageListenerContainer {

	private static final Logger logger = LoggerFactory.getLogger(DefaultMessageListener.class);

	private MessageDealer messageDealer;

	public void setMessageDealer(MessageDealer messageDealer) {
		this.messageDealer = messageDealer;
	}

	public DefaultMessageListener() {
		this.init();
	}

	// 初始化
	private void init() {

		// 设置异常处理类,不处理
		super.setErrorHandler(new ErrorHandler() {

			@Override
			public void handleError(Throwable t) {
				logger.warn("", t);
			}
		});

		this.setMessageListener(new SessionAwareMessageListener<TextMessage>() {

			@Override
			public void onMessage(TextMessage message, Session session) throws JMSException {
				try {
					messageDealer.onMessage(message.getText());
				} catch (MessageQueueException e) {
					throw new JMSException(e.getMessage());
				}
			}
		});

	}

}
