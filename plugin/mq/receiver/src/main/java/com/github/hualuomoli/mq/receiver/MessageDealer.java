package com.github.hualuomoli.mq.receiver;

import com.github.hualuomoli.mq.receiver.lang.MessageListenerException;

/**
 * 消息处理者
 */
public interface MessageDealer {

  /**
   * 处理消息(不要抛出异常,如无法处理需根据实际情况解决)
   * @param data 消息信息
   * @throws MessageListenerException 消息接收错误
   */
  void onMessage(String data) throws MessageListenerException;

}
