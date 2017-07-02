package com.github.hualuomoli.plugin.mq;

/**
 * 消息处理者
 */
public interface MessageDealer {

  /**
   * 处理消息(不要抛出异常,如无法处理需根据实际情况解决)
   * @param data 消息信息
   */
  void onMessage(String data);

}
