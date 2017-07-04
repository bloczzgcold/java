package com.github.hualuomoli.sample.plugin.mq.receiver;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.hualuomoli.sample.plugin.mq.receiver.config.BaseConfig;

@WebAppConfiguration
@ContextConfiguration(classes = { BaseConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ReceiverTest {

  @Test
  public void test() throws IOException {
    System.out.println("mq listener started.");
    System.in.read();
  }
}
