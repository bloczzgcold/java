package com.github.hualuomoli.sample.mq.sender.query.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.hualuomoli.sample.mq.sender.base.entity.User;
import com.github.hualuomoli.sample.mq.sender.query.entity.UserQuery;

// 用户
@Repository(value = "com.github.hualuomoli.sample.mq.sender.query.mapper.UserQueryMapper")
public interface UserQueryMapper {

  /** 查询列表 */
  List<User> findList(UserQuery userQuery);

}
