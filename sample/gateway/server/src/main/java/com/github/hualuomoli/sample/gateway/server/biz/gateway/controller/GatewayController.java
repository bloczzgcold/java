package com.github.hualuomoli.sample.gateway.server.biz.gateway.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.config.gateway.entity.GatewayServerRequest;
import com.github.hualuomoli.config.gateway.entity.GatewayServerResponse;
import com.github.hualuomoli.gateway.server.GatewayServer;

@RequestMapping(value = "/gateway")
@Controller(value = "com.github.hualuomoli.sample.gateway.server.biz.gateway.controller.GatewayController")
public class GatewayController {

  @Autowired
  private GatewayServer<GatewayServerRequest, GatewayServerResponse> gatewayServer;

  @RequestMapping(value = "", method = RequestMethod.POST)
  @ResponseBody
  public String execute(HttpServletRequest req, HttpServletResponse res) {
    GatewayServerResponse response = gatewayServer.execute(req, res);
    return JSON.toJSONString(response);
  }

}
