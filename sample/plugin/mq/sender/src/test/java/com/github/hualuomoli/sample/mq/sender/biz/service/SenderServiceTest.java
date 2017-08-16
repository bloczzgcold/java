package com.github.hualuomoli.sample.mq.sender.biz.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.hualuomoli.sample.mq.sender.ServiceTest;

public class SenderServiceTest extends ServiceTest {

  @Autowired
  private SenderService senderService;

  @Test
  public void testBatchQueue() {
    senderService.batchQueue();
  }

  @Test
  public void testBatchTopic() {
    senderService.batchTopic();
  }

}
