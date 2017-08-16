package com.github.hualuomoli.sample.mq.sender.query.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.framework.entity.Page;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import com.github.hualuomoli.sample.mq.sender.base.entity.User;
import com.github.hualuomoli.sample.mq.sender.query.entity.UserQuery;
import com.github.hualuomoli.sample.mq.sender.query.mapper.UserQueryMapper;

// 用户
@Service(value = "com.github.hualuomoli.sample.mq.sender.query.service.UserQueryService")
@Transactional(readOnly = true)
public class UserQueryService {

  @Autowired
  private UserQueryMapper userQueryMapper;

  /** 查询列表 */
  public List<User> findList(UserQuery userQuery) {
    return userQueryMapper.findList(userQuery);
  }

  /** 查询列表排序 */
  public List<User> findList(UserQuery userQuery, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 查询列表
    return userQueryMapper.findList(userQuery);
  }

  /** 查询分页 */
  public Page findPage(UserQuery userQuery, Integer pageNo, Integer pageSize) {
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<User> list = userQueryMapper.findList(userQuery);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

  /** 查询分页 */
  public Page findPage(UserQuery userQuery, Integer pageNo, Integer pageSize, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<User> list = userQueryMapper.findList(userQuery);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

}
