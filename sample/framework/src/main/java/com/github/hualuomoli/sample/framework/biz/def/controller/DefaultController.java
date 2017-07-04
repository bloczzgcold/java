package com.github.hualuomoli.sample.framework.biz.def.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 默认
 */
@Controller(value = "com.github.hualuomoli.sample.framework.biz.controller.DefaultController")
@RequestMapping(value = "/")
public class DefaultController {

  // 跟请求
  @RequestMapping(value = "")
  public String def(HttpServletRequest req, HttpServletResponse res) {
    return "index";
  }

  // 错误
  @RequestMapping(value = "/error")
  public String error(HttpServletRequest req, HttpServletResponse res) {
    throw new IllegalArgumentException("请求错误");
  }

}
