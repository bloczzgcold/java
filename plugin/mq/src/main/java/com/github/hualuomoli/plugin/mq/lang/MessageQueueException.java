package com.github.hualuomoli.plugin.mq.lang;

/**
 * 消息队列错误
 */
public class MessageQueueException extends RuntimeException {

  private static final long serialVersionUID = -4671991403684287465L;

  public MessageQueueException() {
    super();
  }

  public MessageQueueException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageQueueException(String message) {
    super(message);
  }

  public MessageQueueException(Throwable cause) {
    super(cause);
  }

}
