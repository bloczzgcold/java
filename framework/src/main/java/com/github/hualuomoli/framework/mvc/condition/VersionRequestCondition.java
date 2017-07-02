package com.github.hualuomoli.framework.mvc.condition;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * 根据版本号请求
 */
public class VersionRequestCondition implements RequestCondition<VersionRequestCondition> {

  private Parser parser = new DefaultParser();

  private String version;

  public VersionRequestCondition() {
  }

  public VersionRequestCondition(String version) {
    this.version = version;
  }

  public void setParser(Parser parser) {
    this.parser = parser;
  }

  /**
   * 采用最后定义优先原则
   * 方法上的定义覆盖类上面的定义
   */
  @Override
  public VersionRequestCondition combine(VersionRequestCondition other) {
    return new VersionRequestCondition(other.version);
  }

  // 获取符合的条件
  @Override
  public VersionRequestCondition getMatchingCondition(HttpServletRequest request) {
    // 请求版本号
    String version = parser.getVersion(request);

    return parser.compare(this.version, version) <= 0 ? this : null;
  }

  // 版本号排序,版本号越大越靠前
  @Override
  public int compareTo(VersionRequestCondition other, HttpServletRequest request) {
    return parser.compare(other.version, this.version);
  }

}
