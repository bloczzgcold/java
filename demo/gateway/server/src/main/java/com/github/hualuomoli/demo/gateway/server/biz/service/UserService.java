package com.github.hualuomoli.demo.gateway.server.biz.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.framework.base.entity.Page;
import com.github.hualuomoli.framework.constants.Status;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import com.github.hualuomoli.framework.thread.Current;
import com.github.hualuomoli.demo.gateway.server.biz.entity.User;
import com.github.hualuomoli.demo.gateway.server.biz.mapper.UserMapper;

@Service(value = "com.github.hualuomoli.demo.gateway.server.biz.service.UserService")
@Transactional(readOnly = true)
public class UserService {

	@Autowired
	private UserMapper userMapper;

	public User get(String id) {
		return userMapper.get(id);
	}

	@Transactional(readOnly = false)
	public int insert(User user) {
		if (user.getId() == null) {
			user.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
		}
		user.setCreateBy(Current.getUsername());
		user.setCreateDate(Current.getDate());
		user.setUpdateBy(Current.getUsername());
		user.setUpdateDate(Current.getDate());
		user.setStatus(Status.NOMAL.value());

		return userMapper.insert(user);
	}

	@Transactional(readOnly = false)
	public int update(User user) {

		if (user.getId() == null) {
			return 0;
		}

		user.setCreateBy(null);
		user.setCreateDate(null);
		user.setUpdateBy(Current.getUsername());
		user.setUpdateDate(Current.getDate());

		return userMapper.update(user);
	}

	@Transactional(readOnly = false)
	public int logicDelete(String id) {
		User user = new User();
		user.setId(id);
		user.setStatus(Status.DELETED.value());

		return this.update(user);
	}

	@Transactional(readOnly = false)
	public int delete(String id) {
		return userMapper.delete(id);
	}

	public List<User> findList(User user) {
		return userMapper.findList(user);
	}

	public Page findPage(User user, Integer pageNumber, Integer pageSize, String orderBy) {

		// set page message
		PaginationInterceptor.setPagination(pageNumber, pageSize, orderBy);
		List<User> userList = userMapper.findList(user);
		Integer count = PaginationInterceptor.getCount();
		return new Page(pageNumber, pageSize, count, userList);
	}
}
