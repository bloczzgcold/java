package com.github.hualuomoli.mq.receiver.listener;

import javax.jms.ConnectionFactory;

import com.github.hualuomoli.mq.receiver.MessageDealer;

public class DefaultTopicMessageListener extends DefaultMessageListener {

  public DefaultTopicMessageListener(ConnectionFactory connectionFactory, MessageDealer messageDealer, String clientId) {
    super(connectionFactory, messageDealer);
    // 广播
    this.setClientId(clientId);
    this.setPubSubDomain(true);
    this.setSubscriptionDurable(true);
  }

}
