package com.github.hualuomoli.sample.gateway.server.biz.error.controller;

import org.junit.Assert;
import org.junit.Test;

import com.github.hualuomoli.enums.GatewaySubErrorEnum;
import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.sample.gateway.server.ClientControllerTest;

public class ErrorControllerTest extends ClientControllerTest {

  // 没有访问权限
  @Test
  public void testNoAuth() {
    try {
      client.call("error.noAuth", "{}");
    } catch (BusinessException be) {
      Assert.assertEquals(GatewaySubErrorEnum.INVALID_AUTHORITY.name(), be.getSubCode());
    }
  }

  @Test
  public void testInvalidParameter() {
    try {
      client.call("error.invalidParameter", "{}");
    } catch (BusinessException be) {
      Assert.assertEquals(GatewaySubErrorEnum.INVALID_PARAMETER.name(), be.getSubCode());
    }
  }

  @Test
  public void testRuntime() {
    try {
      client.call("error.runtime", "{}");
    } catch (BusinessException be) {
      Assert.assertEquals(GatewaySubErrorEnum.SYSTEM.name(), be.getSubCode());
    }
  }

}
