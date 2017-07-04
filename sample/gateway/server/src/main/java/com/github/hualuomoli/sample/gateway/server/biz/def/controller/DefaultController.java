package com.github.hualuomoli.sample.gateway.server.biz.def.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "com.github.hualuomoli.sample.gateway.server.biz.def.controller.DefaultController")
@RequestMapping(value = "/")
public class DefaultController {

  @RequestMapping(value = "")
  public String def(HttpServletRequest req, HttpServletResponse res) {
    return "index";
  }

}
