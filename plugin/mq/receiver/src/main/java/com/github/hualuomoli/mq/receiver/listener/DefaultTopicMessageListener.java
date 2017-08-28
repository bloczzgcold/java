package com.github.hualuomoli.mq.receiver.listener;

import javax.jms.ConnectionFactory;

import com.github.hualuomoli.mq.receiver.TopicMessageDealer;

public class DefaultTopicMessageListener extends DefaultMessageListener {

  public DefaultTopicMessageListener(ConnectionFactory connectionFactory, TopicMessageDealer messageDealer) {
    super(connectionFactory, messageDealer);
    // 广播
    this.setClientId(messageDealer.getClientId());
    this.setPubSubDomain(true);
    this.setSubscriptionDurable(true);
  }

}
