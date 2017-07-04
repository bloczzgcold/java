package com.github.hualuomoli.sample.framework.biz.user.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.sample.framework.biz.CurrentThread;
import com.github.hualuomoli.sample.framework.biz.enums.Status;
import com.github.hualuomoli.sample.framework.biz.user.entity.User;
import com.github.hualuomoli.sample.framework.biz.user.mapper.UserMapper;
import com.github.hualuomoli.framework.base.entity.Page;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;

@Service(value = "com.github.hualuomoli.sample.framework.biz.user.service.UserService")
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
    user.setCreateBy(CurrentThread.getOperator());
    user.setCreateDate(CurrentThread.getDate());
    user.setUpdateBy(CurrentThread.getOperator());
    user.setUpdateDate(CurrentThread.getDate());
    user.setStatus(Status.NOMAL.name());

    return userMapper.insert(user);
  }

  @Transactional(readOnly = false)
  public int batchInsert(List<User> userList) {

    if (userList == null || userList.size() == 0) {
      return 0;
    }

    String username = CurrentThread.getOperator();
    Date date = CurrentThread.getDate();

    for (User user : userList) {
      if (user.getId() == null) {
        user.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
      }
      user.setCreateBy(username);
      user.setCreateDate(date);
      user.setUpdateBy(username);
      user.setUpdateDate(date);
      user.setStatus(Status.NOMAL.name());
    }

    return userMapper.batchInsert(userList);
  }

  @Transactional(readOnly = false)
  public int batchInsert(User[] users) {

    if (users == null || users.length == 0) {
      return 0;
    }

    String username = CurrentThread.getOperator();
    Date date = CurrentThread.getDate();

    for (User user : users) {
      if (user.getId() == null) {
        user.setId(UUID.randomUUID().toString().replaceAll("[-]", ""));
      }
      user.setCreateBy(username);
      user.setCreateDate(date);
      user.setUpdateBy(username);
      user.setUpdateDate(date);
      user.setStatus(Status.NOMAL.name());
    }

    return userMapper.batchInsert(users);
  }

  @Transactional(readOnly = false)
  public int update(User user) {

    if (user.getId() == null) {
      return 0;
    }

    user.setCreateBy(null);
    user.setCreateDate(null);
    user.setUpdateBy(CurrentThread.getOperator());
    user.setUpdateDate(CurrentThread.getDate());

    return userMapper.update(user);
  }

  @Transactional(readOnly = false)
  public int logicDelete(String id) {
    User user = new User();
    user.setId(id);
    user.setStatus(Status.DELETED.name());

    return this.update(user);
  }

  @Transactional(readOnly = false)
  public int delete(String id) {
    return userMapper.delete(id);
  }

  @Transactional(readOnly = false)
  public int batchDelete(List<String> ids) {
    if (ids == null || ids.size() == 0) {
      return 0;
    }
    return userMapper.batchDelete(ids);
  }

  @Transactional(readOnly = false)
  public int batchDelete(String[] ids) {
    if (ids == null || ids.length == 0) {
      return 0;
    }
    return userMapper.batchDelete(ids);
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
