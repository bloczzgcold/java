package com.github.hualuomoli.mq.receiver;

/**
 * 消息处理者
 */
public interface MessageDealer {

  /**
   * 获取监听目的地名称
   * @return 目的地名称
   */
  String getDestinationName();

  /**
   * 处理消息(不要抛出异常,如无法处理需根据实际情况解决)
   * @param data 消息信息
   * @throws Exception 业务处理错误
   */
  void onMessage(String data) throws Exception;

  /**
   * 业务处理错误{@linkplain #onMessage(String)}
   * @param e 错误信息
   */
  void onError(Exception e);

}
