package com.github.hualuomoli.sample.gateway.server.biz.error.controller;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/error")
@Controller(value = "com.github.hualuomoli.sample.gateway.server.biz.error.controller.ErrorController")
public class ErrorController {

  // 没有访问权限
  // 在业务拦截器处理
  @RequestMapping(value = "/noAuth")
  @ResponseBody
  public Object noAuth() {
    return null;
  }

  // 参数错误
  // 在业务拦截器处理
  @RequestMapping(value = "/invalidParameter")
  @ResponseBody
  public Object invalidParameter(NoParameterRequest req) {
    return null;
  }

  public static class NoParameterRequest {

    @NotBlank
    private String userName;

  }

  // 业务运行错误
  @RequestMapping(value = "/runtime")
  @ResponseBody
  public Object runtime() {
    throw new RuntimeException("运行时出错咯.");
  }

}
