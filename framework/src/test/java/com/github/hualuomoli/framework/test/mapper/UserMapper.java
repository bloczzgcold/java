package com.github.hualuomoli.framework.test.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.github.hualuomoli.framework.test.entity.User;

@Repository(value = "com.github.hualuomoli.framework.test.mapper.UserMapper")
public interface UserMapper {

	User get(String id);

	int insert(User user);

	int update(User user);

	int delete(String id);

	List<User> findList(User user);
}
