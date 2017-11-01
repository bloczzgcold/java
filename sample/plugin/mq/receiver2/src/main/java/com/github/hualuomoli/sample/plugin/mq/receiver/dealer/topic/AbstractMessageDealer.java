package com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic;

import com.github.hualuomoli.mq.receiver.MessageDealer;

public abstract class AbstractMessageDealer implements MessageDealer {

  private String destinationName = "sample_topic";

  @Override
  public String getDestinationName() {
    return destinationName;
  }

  @Override
  public void onError(String data, Exception e) {
    e.printStackTrace();
  }

}
