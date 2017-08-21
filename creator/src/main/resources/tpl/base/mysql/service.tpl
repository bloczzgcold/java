package ${packageName}.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.hualuomoli.framework.entity.Page;
import com.github.hualuomoli.framework.plugin.mybatis.interceptor.pagination.PaginationInterceptor;
import ${packageName}.entity.${javaName};
import ${packageName}.mapper.${javaName}BaseMapper;

// ${comment!''}
@Service(value = "${packageName}.service.${javaName}BaseService")
@Transactional(readOnly = true)
public class ${javaName}BaseService {

  @Autowired
  private ${javaName}BaseMapper ${javaName?uncap_first}BaseMapper;
  <#list columns as column>
    <#if column.primary>

  /** 根据主键${column.javaName}查询 */
  public ${javaName} get(${column.javaTypeName} ${column.javaName}) {
    return ${javaName?uncap_first}BaseMapper.get(${column.javaName});
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引${column.javaName}查询 */
  public ${javaName} findBy${column.javaName?cap_first}(${column.javaTypeName} ${column.javaName}) {
    return ${javaName?uncap_first}BaseMapper.findBy${column.javaName?cap_first}(${column.javaName});
  }
    </#if>
  </#list>

  /** 添加 */
  @Transactional(readOnly = false)
  public int insert(${javaName} ${javaName?uncap_first}) {
    return ${javaName?uncap_first}BaseMapper.insert(${javaName?uncap_first});
  }

  /** 批量添加 */
  @Transactional(readOnly = false)
  public <T extends ${javaName}> int batchInsert(List<T> list, int fetchSize) {
    if (list == null || list.size() == 0) {
      return 0;
    }
    return ${javaName?uncap_first}BaseMapper.batchInsert(list);
  }
  <#list columns as column>
    <#if column.primary>

  /** 根据主键${column.javaName}修改 */
  @Transactional(readOnly = false)
  public int update(${javaName} ${javaName?uncap_first}) {
    return ${javaName?uncap_first}BaseMapper.update(${javaName?uncap_first});
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引修改 */
  @Transactional(readOnly = false)
  public int updateBy${column.javaName?cap_first}(${column.javaTypeName} ${column.javaName}, ${javaName} ${javaName?uncap_first}) {
    ${javaName?uncap_first}.set${column.javaName?cap_first}(${column.javaName});
    return ${javaName?uncap_first}BaseMapper.updateBy${column.javaName?cap_first}(${javaName?uncap_first});
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.primary>

  /** 根据主键删除 */
  @Transactional(readOnly = false)
  public int delete(${column.javaTypeName} ${column.javaName}) {
    return ${javaName?uncap_first}BaseMapper.delete(${column.javaName});
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引删除 */
  @Transactional(readOnly = false)
  public int deleteBy${column.javaName?cap_first}(${column.javaTypeName} ${column.javaName}) {
    return ${javaName?uncap_first}BaseMapper.deleteBy${column.javaName?cap_first}(${column.javaName});
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.primary>

  /** 根据主键批量删除 */
  @Transactional(readOnly = false)
  public int deleteByArray(${column.javaTypeName}[] ${column.javaName}s) {
    if (${column.javaName}s == null || ${column.javaName}s.length == 0) {
      return 0;
    }
    return ${javaName?uncap_first}BaseMapper.deleteByArray(${column.javaName}s);
  }
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引批量删除 */
  @Transactional(readOnly = false)
  public int deleteBy${column.javaName?cap_first}Array(${column.javaTypeName}[] ${column.javaName}s) {
    if (${column.javaName}s == null || ${column.javaName}s.length == 0) {
      return 0;
    }
    return ${javaName?uncap_first}BaseMapper.deleteBy${column.javaName?cap_first}Array(${column.javaName}s);
  }
    </#if>
  </#list>

  /** 查询列表 */
  public List<${javaName}> findList(${javaName} ${javaName?uncap_first}) {
    return ${javaName?uncap_first}BaseMapper.findList(${javaName?uncap_first});
  }

  /** 查询列表排序 */
  public List<${javaName}> findList(${javaName} ${javaName?uncap_first}, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 查询列表
    return ${javaName?uncap_first}BaseMapper.findList(${javaName?uncap_first});
  }

  /** 查询分页 */
  public Page findPage(${javaName} ${javaName?uncap_first}, Integer pageNo, Integer pageSize) {
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<${javaName}> list = ${javaName?uncap_first}BaseMapper.findList(${javaName?uncap_first});
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

  /** 查询分页 */
  public Page findPage(${javaName} ${javaName?uncap_first}, Integer pageNo, Integer pageSize, String orderBy) {
    // 设置排序
    PaginationInterceptor.setOrderBy(orderBy);
    // 设置分页
    PaginationInterceptor.setPagination(pageNo, pageSize);
    // 查询
    List<${javaName}> list = ${javaName?uncap_first}BaseMapper.findList(${javaName?uncap_first});
    // 总数量
    int count = PaginationInterceptor.getCount();
    // 组装
    return new Page(pageNo, pageSize, count, list);
  }

}
