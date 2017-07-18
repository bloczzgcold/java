package com.github.hualuomoli.mq.receiver.lang;

/**
 * 消息接收错误
 */
public class MessageListenerException extends RuntimeException {

  private static final long serialVersionUID = -4800636639193648071L;

  public MessageListenerException() {
    super();
  }

  public MessageListenerException(String message, Throwable cause) {
    super(message, cause);
  }

  public MessageListenerException(String message) {
    super(message);
  }

  public MessageListenerException(Throwable cause) {
    super(cause);
  }

}
