package com.github.hualuomoli.sample.atomikos.ds2.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.framework.entity.Page;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import com.github.hualuomoli.sample.atomikos.ds2.base.entity.Address;
import com.github.hualuomoli.sample.atomikos.ds2.base.mapper.AddressBaseMapper;

// 收货地址
@Service(value = "com.github.hualuomoli.sample.atomikos.ds2.base.service.AddressBaseService")
@Transactional(readOnly = true)
public class AddressBaseService {

  @Autowired
  private AddressBaseMapper addressBaseMapper;

  /** 根据主键id查询 */
  public Address get(java.lang.String id) {
    return addressBaseMapper.get(id);
  }

  /** 添加 */
  @Transactional(readOnly = false)
  public int insert(Address address) {
    return addressBaseMapper.insert(address);
  }

  /** 批量添加 */
  @Transactional(readOnly = false)
  public <T extends Address> int batchInsert(List<T> list, int fetchSize) {
    if (list == null || list.size() == 0) {
      return 0;
    }
    return addressBaseMapper.batchInsert(list);
  }

  /** 根据主键id修改 */
  @Transactional(readOnly = false)
  public int update(Address address) {
    return addressBaseMapper.update(address);
  }

  /** 根据主键删除 */
  @Transactional(readOnly = false)
  public int delete(java.lang.String id) {
    return addressBaseMapper.delete(id);
  }

  /** 根据主键批量删除 */
  @Transactional(readOnly = false)
  public int deleteByArray(java.lang.String[] ids) {
    if (ids == null || ids.length == 0) {
      return 0;
    }
    return addressBaseMapper.deleteByArray(ids);
  }

  /** 查询列表 */
  public List<Address> findList(Address address) {
    return addressBaseMapper.findList(address);
  }

  /** 查询列表排序 */
  public List<Address> findList(Address address, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 查询列表
    return addressBaseMapper.findList(address);
  }

  /** 查询分页 */
  public Page findPage(Address address, Integer pageNo, Integer pageSize) {
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<Address> list = addressBaseMapper.findList(address);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

  /** 查询分页 */
  public Page findPage(Address address, Integer pageNo, Integer pageSize, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<Address> list = addressBaseMapper.findList(address);
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

}
