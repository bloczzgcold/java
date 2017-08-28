package com.github.hualuomoli.mq.receiver;

/**
 * 广播
 */
public interface TopicMessageDealer extends MessageDealer {

  /**
   * 获取客户端唯一ID
   * @return 客户端唯一ID
   */
  String getClientId();

}
