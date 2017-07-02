package com.github.hualuomoli.plugin.mq.listener;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.github.hualuomoli.plugin.mq.MessageDealer;
import com.github.hualuomoli.plugin.mq.lang.MessageQueueException;

/**
 * 默认消息监听器
 */
public class DefaultMessageListener extends DefaultMessageListenerContainer {

  private MessageDealer messageDealer;

  public void setMessageDealer(MessageDealer messageDealer) {
    this.messageDealer = messageDealer;
  }

  public DefaultMessageListener() {
    this.init();
  }

  // 初始化
  private void init() {

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
