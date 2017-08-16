package com.github.hualuomoli.sample.atomikos.biz.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.hualuomoli.sample.atomikos.ServiceTest;

public class JtaServiceTest extends ServiceTest {

  @Autowired
  private JtaService jtaService;

  @Test
  public void test() {
    jtaService.execute();
  }

  @Test(expected = Exception.class)
  public void testSecondError() {
    jtaService.secondError();
  }

  @Test(expected = Exception.class)
  public void testFirstError() {
    jtaService.firstError();
  }

}
