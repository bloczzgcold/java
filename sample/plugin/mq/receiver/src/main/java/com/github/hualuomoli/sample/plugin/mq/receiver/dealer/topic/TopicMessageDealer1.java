package com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.mq.receiver.TopicMessageDealer;
import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.service.ShowService;

@Service(value = "com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic.TopicMessageDealer1")
public class TopicMessageDealer1 extends AbstractMessageDealer implements TopicMessageDealer {

  @Autowired
  private ShowService showService;

  @Override
  public void onMessage(String data) {
    showService.show(TopicMessageDealer1.class, data);
  }

  @Override
  public String getClientId() {
    return "client1";
  }

}
