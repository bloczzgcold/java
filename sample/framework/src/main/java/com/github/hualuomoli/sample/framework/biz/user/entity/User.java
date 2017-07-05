package com.github.hualuomoli.sample.framework.biz.user.entity;

import com.github.hualuomoli.sample.framework.biz.enums.StateEnum;
import com.github.hualuomoli.sample.framework.biz.enums.StatusEnum;

/**
 * 用户
 */
public class User {

  /** id(UUID) */
  private String id;
  /** 用户名 */
  private String username;
  /** 昵称 */
  private String nickname;
  /** 年龄 */
  private Integer age;
  /** 性别 */
  private String sex;
  /** 数据状态 */
  private StateEnum state;
  /** 数据状态 */
  private StatusEnum status;
  /** 描述信息 */
  private String remark;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", nickname=" + nickname + ", age=" + age + ", sex=" + sex + ", state=" + state + ", status=" + status
        + ", remark=" + remark + "]";
  }

}
