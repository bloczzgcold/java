package com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.plugin.mq.MessageDealer;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.service.ShowService;

@Service(value = "com.github.hualuomoli.sample.plugin.mq.receiver.dealer.queue.QueueMessageDealer2")
public class QueueMessageDealer2 implements MessageDealer {

  @Autowired
  private ShowService showService;

  @Override
  public void onMessage(String data) {
    showService.show(QueueMessageDealer2.class, data);
  }

}
