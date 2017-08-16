package com.github.hualuomoli.sample.mq.sender.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.github.hualuomoli.sample.mq.sender.base.entity.User;

// 用户
@Repository(value = "com.github.hualuomoli.sample.mq.sender.base.mapper.UserBaseMapper")
public interface UserBaseMapper {

  /** 根据主键id查询 */
  User get(java.lang.String id);

  /** 根据唯一索引username查询 */
  User findByUsername(java.lang.String username);

  /** 添加 */
  int insert(User user);

  /** 批量添加 */
  <T extends User> int batchInsert(@Param(value = "list") List<T> list);

  /** 根据主键id修改 */
  int update(User user);

  /** 根据唯一索引修改 */
  int updateByUsername(User user);

  /** 根据主键删除 */
  int delete(java.lang.String id);

  /** 根据唯一索引删除 */
  int deleteByUsername(java.lang.String username);

  /** 根据主键批量删除 */
  int deleteByArray(@Param(value = "ids") java.lang.String[] id);

  /** 根据唯一索引批量删除 */
  int deleteByUsernameArray(@Param(value = "usernames") java.lang.String[] username);

  /** 查询列表 */
  List<User> findList(User user);

}
