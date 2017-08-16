package com.github.hualuomoli.sample.atomikos.ds1.query.entity;

import com.github.hualuomoli.sample.atomikos.ds1.base.entity.User;

// 用户信息
public class UserQuery extends User {

  /** 用户名 */
  private java.lang.String usernameLeftLike;
  private java.lang.String usernameRightLike;
  private java.lang.String usernameLike;
  /** 昵称 */
  private java.lang.String nicknameLeftLike;
  private java.lang.String nicknameRightLike;
  private java.lang.String nicknameLike;
  /** 年龄 */
  private java.lang.Integer ageGreaterThan;
  private java.lang.Integer ageGreaterEqual;
  private java.lang.Integer ageLessEqual;
  private java.lang.Integer ageLessThan;
  /** 性别 */
  private java.lang.String sexLeftLike;
  private java.lang.String sexRightLike;
  private java.lang.String sexLike;
  /** 数据状态1=正常 */
  private java.lang.Integer stateGreaterThan;
  private java.lang.Integer stateGreaterEqual;
  private java.lang.Integer stateLessEqual;
  private java.lang.Integer stateLessThan;
  /** 数据状态 */
  private com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] statusIns;
  private com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] statusNotIns;
  /** 描述信息 */
  private java.lang.String remarkLeftLike;
  private java.lang.String remarkRightLike;
  private java.lang.String remarkLike;

  public UserQuery() {
  }

  // getter and setter

  public java.lang.String getUsernameLeftLike() {
    return usernameLeftLike;
  }

  public void setUsernameLeftLike(java.lang.String usernameLeftLike) {
    this.usernameLeftLike = usernameLeftLike;
  }

  public java.lang.String getUsernameRightLike() {
    return usernameRightLike;
  }

  public void setUsernameRightLike(java.lang.String usernameRightLike) {
    this.usernameRightLike = usernameRightLike;
  }

  public java.lang.String getUsernameLike() {
    return usernameLike;
  }

  public void setUsernameLike(java.lang.String usernameLike) {
    this.usernameLike = usernameLike;
  }

  public java.lang.String getNicknameLeftLike() {
    return nicknameLeftLike;
  }

  public void setNicknameLeftLike(java.lang.String nicknameLeftLike) {
    this.nicknameLeftLike = nicknameLeftLike;
  }

  public java.lang.String getNicknameRightLike() {
    return nicknameRightLike;
  }

  public void setNicknameRightLike(java.lang.String nicknameRightLike) {
    this.nicknameRightLike = nicknameRightLike;
  }

  public java.lang.String getNicknameLike() {
    return nicknameLike;
  }

  public void setNicknameLike(java.lang.String nicknameLike) {
    this.nicknameLike = nicknameLike;
  }

  public java.lang.Integer getAgeGreaterThan() {
    return ageGreaterThan;
  }

  public void setageGreaterThan(java.lang.Integer ageGreaterThan) {
    this.ageGreaterThan = ageGreaterThan;
  }

  public java.lang.Integer getAgeGreaterEqual() {
    return ageGreaterEqual;
  }

  public void setAgeGreaterEqual(java.lang.Integer ageGreaterEqual) {
    this.ageGreaterEqual = ageGreaterEqual;
  }

  public java.lang.Integer getAgeLessEqual() {
    return ageLessEqual;
  }

  public void setAgeLessEqual(java.lang.Integer ageLessEqual) {
    this.ageLessEqual = ageLessEqual;
  }

  public java.lang.Integer getAgeLessThan() {
    return ageLessThan;
  }

  public void setAgeLessThan(java.lang.Integer ageLessThan) {
    this.ageLessThan = ageLessThan;
  }

  public java.lang.String getSexLeftLike() {
    return sexLeftLike;
  }

  public void setSexLeftLike(java.lang.String sexLeftLike) {
    this.sexLeftLike = sexLeftLike;
  }

  public java.lang.String getSexRightLike() {
    return sexRightLike;
  }

  public void setSexRightLike(java.lang.String sexRightLike) {
    this.sexRightLike = sexRightLike;
  }

  public java.lang.String getSexLike() {
    return sexLike;
  }

  public void setSexLike(java.lang.String sexLike) {
    this.sexLike = sexLike;
  }

  public java.lang.Integer getStateGreaterThan() {
    return stateGreaterThan;
  }

  public void setstateGreaterThan(java.lang.Integer stateGreaterThan) {
    this.stateGreaterThan = stateGreaterThan;
  }

  public java.lang.Integer getStateGreaterEqual() {
    return stateGreaterEqual;
  }

  public void setStateGreaterEqual(java.lang.Integer stateGreaterEqual) {
    this.stateGreaterEqual = stateGreaterEqual;
  }

  public java.lang.Integer getStateLessEqual() {
    return stateLessEqual;
  }

  public void setStateLessEqual(java.lang.Integer stateLessEqual) {
    this.stateLessEqual = stateLessEqual;
  }

  public java.lang.Integer getStateLessThan() {
    return stateLessThan;
  }

  public void setStateLessThan(java.lang.Integer stateLessThan) {
    this.stateLessThan = stateLessThan;
  }

  public com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] getStatusIns() {
    return statusIns;
  }

  public void setStatusIns(com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] statusIns) {
    this.statusIns = statusIns;
  }

  public com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] getStatusNotIns() {
    return statusNotIns;
  }

  public void setStatusNotIns(com.github.hualuomoli.sample.atomikos.enums.StatusEnum[] statusNotIns) {
    this.statusNotIns = statusNotIns;
  }

  public java.lang.String getRemarkLeftLike() {
    return remarkLeftLike;
  }

  public void setRemarkLeftLike(java.lang.String remarkLeftLike) {
    this.remarkLeftLike = remarkLeftLike;
  }

  public java.lang.String getRemarkRightLike() {
    return remarkRightLike;
  }

  public void setRemarkRightLike(java.lang.String remarkRightLike) {
    this.remarkRightLike = remarkRightLike;
  }

  public java.lang.String getRemarkLike() {
    return remarkLike;
  }

  public void setRemarkLike(java.lang.String remarkLike) {
    this.remarkLike = remarkLike;
  }

}
