package com.github.hualuomoli.apidoc.entity;

import java.util.Arrays;
import java.util.List;

/**
 * API文档
 */
public class ApiDoc {

  /** 模块 */
  private String[] modules;
  /** 方法名 */
  private String method;
  /** 功能描述 */
  private String title;
  /** 功能详细描述 */
  private String description;

  /** 请求参数 */
  private List<Parameter> requests;
  /** 响应参数 */
  private List<Parameter> responses;

  public String[] getModules() {
    return modules;
  }

  public void setModules(String[] modules) {
    this.modules = modules;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Parameter> getRequests() {
    return requests;
  }

  public void setRequests(List<Parameter> requests) {
    this.requests = requests;
  }

  public List<Parameter> getResponses() {
    return responses;
  }

  public void setResponses(List<Parameter> responses) {
    this.responses = responses;
  }

  @Override
  public String toString() {
    return "ApiDoc [modules=" + Arrays.toString(modules) + ", method=" + method + ", title=" + title + ", description=" + description + ", requests=" + requests + ", responses=" + responses + "]";
  }

}
