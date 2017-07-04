package com.github.hualuomoli.sample.framework.biz.user.entity;

import java.util.Date;

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
  /** 创建人 */
  private String createBy;
  /** 创建时间 */
  private Date createDate;
  /** 修改人 */
  private String updateBy;
  /** 修改时间 */
  private Date updateDate;
  /** 数据状态 */
  private String status;
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

  public String getCreateBy() {
    return createBy;
  }

  public void setCreateBy(String createBy) {
    this.createBy = createBy;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(String updateBy) {
    this.updateBy = updateBy;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
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
    return "User [id=" + id + ", username=" + username + ", nickname=" + nickname + ", age=" + age + ", sex=" + sex + ", createBy=" + createBy + ", createDate="
        + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", status=" + status + ", remark=" + remark + "]";
  }

}
