package com.github.hualuomoli.sample.framework.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.framework.entity.Page;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import com.github.hualuomoli.sample.framework.base.entity.User;
import com.github.hualuomoli.sample.framework.base.mapper.UserBaseMapper;

// 用户
@Service(value = "com.github.hualuomoli.sample.framework.base.service.UserBaseService")
@Transactional(readOnly = true)
public class UserBaseService {

  @Autowired
  private UserBaseMapper userBaseMapper;

  /** 根据主键id查询 */
  public User get(java.lang.String id) {
    return userBaseMapper.get(id);
  }

  /** 根据唯一索引username查询 */
  public User findByUsername(java.lang.String username) {
    return userBaseMapper.findByUsername(username);
  }

  /** 添加 */
  @Transactional(readOnly = false)
  public int insert(User user) {
    return userBaseMapper.insert(user);
  }

  /** 批量添加 */
  @Transactional(readOnly = false)
  public <T extends User> int batchInsert(List<T> list, int fetchSize) {
    if (list == null || list.size() == 0) {
      return 0;
    }
    return userBaseMapper.batchInsert(list);
  }

  /** 根据主键id修改 */
  @Transactional(readOnly = false)
  public int update(User user) {
    return userBaseMapper.update(user);
  }

  /** 根据唯一索引修改 */
  @Transactional(readOnly = false)
  public int updateByUsername(java.lang.String username, User user) {
    user.setUsername(username);
    return userBaseMapper.updateByUsername(user);
  }

  /** 根据主键删除 */
  @Transactional(readOnly = false)
  public int delete(java.lang.String id) {
    return userBaseMapper.delete(id);
  }

  /** 根据唯一索引删除 */
  @Transactional(readOnly = false)
  public int deleteByUsername(java.lang.String username) {
    return userBaseMapper.deleteByUsername(username);
  }

  /** 根据主键批量删除 */
  @Transactional(readOnly = false)
  public int deleteByArray(java.lang.String[] ids) {
    if (ids == null || ids.length == 0) {
      return 0;
    }
    return userBaseMapper.deleteByArray(ids);
  }

  /** 根据唯一索引批量删除 */
  @Transactional(readOnly = false)
  public int deleteByUsernameArray(java.lang.String[] usernames) {
    if (usernames == null || usernames.length == 0) {
      return 0;
    }
    return userBaseMapper.deleteByUsernameArray(usernames);
  }

  /** 查询列表 */
  public List<User> findList(User user) {
    return userBaseMapper.findList(user);
  }

  /** 查询列表排序 */
  public List<User> findList(User user, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 查询列表
    return userBaseMapper.findList(user);
  }

  /** 查询分页 */
  public Page findPage(User user, Integer pageNo, Integer pageSize) {
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<User> list = userBaseMapper.findList(user);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

  /** 查询分页 */
  public Page findPage(User user, Integer pageNo, Integer pageSize, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<User> list = userBaseMapper.findList(user);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

}
