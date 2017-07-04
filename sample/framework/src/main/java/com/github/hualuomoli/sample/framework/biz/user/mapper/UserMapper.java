package com.github.hualuomoli.sample.framework.biz.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.hualuomoli.sample.framework.biz.user.entity.User;

@Repository(value = "com.github.hualuomoli.sample.framework.biz.user.mapper.UserMapper")
public interface UserMapper {

  User get(String id);

  int insert(User user);

  int batchInsert(@Param(value = "list") List<User> list);

  int batchInsert(@Param(value = "list") User[] list);

  int update(User user);

  int delete(String id);

  int batchDelete(@Param(value = "ids") List<String> ids);

  int batchDelete(@Param(value = "ids") String[] ids);

  List<User> findList(User user);
}
