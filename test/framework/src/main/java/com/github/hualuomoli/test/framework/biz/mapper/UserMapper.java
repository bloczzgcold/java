package com.github.hualuomoli.test.framework.biz.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.hualuomoli.test.framework.biz.entity.User;

@Repository(value = "com.github.hualuomoli.test.framework.biz.mapper.UserMapper")
public interface UserMapper {

	User get(String id);

	int insert(User user);

	int update(User user);

	int delete(String id);

	List<User> findList(User user);
}
