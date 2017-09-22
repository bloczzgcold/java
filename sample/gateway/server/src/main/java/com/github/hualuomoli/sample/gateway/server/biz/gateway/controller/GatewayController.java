package com.github.hualuomoli.sample.gateway.server.biz.gateway.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.config.gateway.entity.GatewayServerResponse;
import com.github.hualuomoli.gateway.server.GatewayServer;
import com.github.hualuomoli.gateway.server.lang.NoPartnerException;
import com.github.hualuomoli.gateway.server.lang.NoRouterException;
import com.github.hualuomoli.gateway.server.lang.SecurityException;

@RequestMapping(value = "/gateway")
@Controller(value = "com.github.hualuomoli.sample.gateway.server.biz.gateway.controller.GatewayController")
public class GatewayController {

  private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

  @Autowired
  private GatewayServer gatewayServer;

  @RequestMapping(value = "", method = RequestMethod.POST)
  @ResponseBody
  public String execute(HttpServletRequest req, HttpServletResponse res) {
    // 执行
    GatewayServerResponse response = new GatewayServerResponse();
    try {
      response = gatewayServer.execute(req, res, GatewayServerResponse.class);
    } catch (NoPartnerException npe) {
      response.setCode("NO_PARTNER");
      response.setMessage("合作伙伴未注册");
    } catch (SecurityException se) {
      response.setCode("INVALID_SECURITY");
      response.setMessage("认证失败");
    } catch (NoRouterException se) {
      response.setCode("NO_ROUTER");
      response.setMessage("方法未注册");
    } catch (Exception e) {
      response.setCode("SUCCESS");
      response.setMessage("执行成功");
      response.setSubCode("9999");
      response.setSubMessage(e.getMessage());
      logger.debug("错误", e);
    }

    return JSON.toJSONString(response);
  }

}
