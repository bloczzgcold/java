package ${packageName}.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import ${packageName}.base.entity.${javaName};

// ${comment!''}
@Repository(value = "${packageName}.base.mapper.${javaName}BaseMapper")
public interface ${javaName}BaseMapper {
  <#list columns as column>
    <#if column.primary>

  /** 根据主键${column.javaName}查询 */
  ${javaName} get(${column.javaTypeName} ${column.javaName});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引${column.javaName}查询 */
  ${javaName} findBy${column.javaName?cap_first}(${column.javaTypeName} ${column.javaName});
    </#if>
  </#list>

  /** 添加 */
  int insert(${javaName} ${javaName?uncap_first});

  /** 批量添加 */
  <T extends ${javaName}> int batchInsert(@Param(value = "list") List<T> list);
  <#list columns as column>
    <#if column.primary>

  /** 根据主键${column.javaName}修改 */
  int update(${javaName} ${javaName?uncap_first});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引修改 */
  int updateBy${column.javaName?cap_first}(${javaName} ${javaName?uncap_first});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.primary>

  /** 根据主键删除 */
  int delete(${column.javaTypeName} ${column.javaName});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引删除 */
  int deleteBy${column.javaName?cap_first}(${column.javaTypeName} ${column.javaName});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.primary>

  /** 根据主键批量删除 */
  int deleteByArray(@Param(value = "${column.javaName}s") ${column.javaTypeName}[] ${column.javaName});
    </#if>
  </#list>
  <#list columns as column>
    <#if column.unique>

  /** 根据唯一索引批量删除 */
  int deleteBy${column.javaName?cap_first}Array(@Param(value = "${column.javaName}s") ${column.javaTypeName}[] ${column.javaName});
    </#if>
  </#list>

  /** 查询列表 */
  List<${javaName}> findList(${javaName} ${javaName?uncap_first});

}
