package com.github.hualuomoli.plugin.mq;

import com.github.hualuomoli.plugin.mq.lang.MessageQueueException;

/**
 * 消息发送者
 * @author lbq
 *
 */
public interface MessageSender {

	/**
	 * 发送消息到消息队列,默认点对点
	 * @param destinationName 消息名称
	 * @param data 数据
	 * @throws MessageQueueException 发送失败
	 */
	void send(String destinationName, final String data) throws MessageQueueException;

	/**
	 * 发送消息到消息队列
	 * @param destinationName 消息名称
	 * @param data 数据
	 * @param type MQ类型
	 * @throws MessageQueueException 发送失败
	 */
	void send(String destinationName, final String data, Type type) throws MessageQueueException;

	// MQ类型
	static enum Type {
		/** 点对点 */
		QUEUE(),
		/** 广播 */
		TOPIC();

	}

}
