package com.github.hualuomoli.test.framework.biz.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.hualuomoli.test.framework.biz.entity.User;

@Repository(value = "com.github.hualuomoli.test.framework.biz.mapper.UserMapper")
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
