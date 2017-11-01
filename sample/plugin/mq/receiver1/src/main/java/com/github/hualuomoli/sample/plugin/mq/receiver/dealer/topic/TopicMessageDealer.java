package com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.sample.plugin.mq.receiver.dealer.service.ShowService;
import com.github.hualuomoli.util.ProjectConfig;

@Service(value = "com.github.hualuomoli.sample.plugin.mq.receiver.dealer.topic.TopicMessageDealer")
public class TopicMessageDealer extends AbstractMessageDealer implements com.github.hualuomoli.mq.receiver.TopicMessageDealer {

  @Autowired
  private ShowService showService;

  @Override
  public void onMessage(String data) {
    showService.show(TopicMessageDealer.class, data);
  }

  @Override
  public String getClientId() {
    return ProjectConfig.getString("activemq.topic.clientId");
  }

}
