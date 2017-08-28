package com.github.hualuomoli.mq.receiver.listener;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.github.hualuomoli.mq.receiver.MessageDealer;

/**
 * 默认消息监听器
 */
public class DefaultMessageListener extends DefaultMessageListenerContainer {

  public DefaultMessageListener(ConnectionFactory connectionFactory, MessageDealer messageDealer) {
    // 连接工厂
    this.setConnectionFactory(connectionFactory);
    // 目的地
    this.setDestinationName(messageDealer.getDestinationName());
    // 监听器
    this.setMessageListener(new TextMessageListener(messageDealer));
  }

  // 文本监听器
  private class TextMessageListener implements SessionAwareMessageListener<TextMessage> {

    private MessageDealer messageDealer;

    TextMessageListener(MessageDealer messageDealer) {
      this.messageDealer = messageDealer;
    }

    @Override
    public void onMessage(TextMessage message, Session session) throws JMSException {
      try {
        messageDealer.onMessage(message.getText());
      } catch (Exception e) {
        try {
          messageDealer.onError(e);
        } catch (Exception e1) {
          throw new JMSException(e1.getMessage());
        }
      }
    }

  }

}
