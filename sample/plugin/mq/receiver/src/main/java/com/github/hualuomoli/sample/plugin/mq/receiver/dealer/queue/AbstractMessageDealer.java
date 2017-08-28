package com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue;

import com.github.hualuomoli.mq.receiver.MessageDealer;

public abstract class AbstractMessageDealer implements MessageDealer {

  private String destinationName = "sample_queue";

  @Override
  public String getDestinationName() {
    return destinationName;
  }

  @Override
  public void onError(Exception e) {
    e.printStackTrace();
  }

}
