<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.base.mapper.${javaName}BaseMapper">

  <!-- 返回的列 -->
  <sql id="columns">
  <#list columns as column>
    `${column.dbName}` as "${column.javaName}"<#if columns?size - column_index gt 1>,</#if>
  </#list>
  </sql>
  
  <!-- 查询的列 -->
  <sql id="querys">
  <#list columns as column>
    <#if column.javaTypeName == 'java.lang.String'>
    <#-- 字符串 -->
    <if test="${column.javaName} != null and ${column.javaName} != ''"> 
      and `${column.dbName}` = ${r"#{"}${column.javaName}${r"}"}
    </if>
    <#else>
    <#-- 非字符串普通类型 -->
    <if test="${column.javaName} != null"> 
      and `${column.dbName}` = ${r"#{"}${column.javaName}${r"}"}
    </if>
    </#if>
  </#list>
  </sql>
  
  <#list columns as column>
    <#if column.primary>
  <!-- 根据主键${column.javaName}查询 -->
  <select id="get" resultType="${packageName}.base.entity.${javaName}">
    select 
      <include refid="columns" />
    from `${tableName}`
    where `${column.dbName}` =  ${r"#{"}${column.javaName}${r"}"}
  </select>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.unique>
  <!-- 根据唯一索引${column.javaName}查询 -->
  <select id="findBy${column.javaName?cap_first}" resultType="${packageName}.base.entity.${javaName}">
    select 
      <include refid="columns" />
    from `${tableName}`
    where `${column.dbName}` =  ${r"#{"}${column.javaName}${r"}"}
  </select>
    </#if>
  </#list>

  <!-- 添加 -->
  <insert id="insert">
    insert into `${tableName}` (
    <#list columns as column>
      `${column.dbName}`<#if columns?size - column_index gt 1>,</#if>
    </#list>  
    ) values (
    <#list columns as column>
      ${r"#{"}${column.javaName}${r"}"}<#if columns?size - column_index gt 1>,</#if>
    </#list>
    )
  </insert>

  <!-- 批量添加 -->
  <insert id="batchInsert">
    insert into `${tableName}` (
    <#list columns as column>
      `${column.dbName}`<#if columns?size - column_index gt 1>,</#if>
    </#list>  
    ) 
    <foreach collection="list" item="obj" separator="union all">
    select
      <#list columns as column>
      ${r"#{"}obj.${column.javaName}${r"}"}<#if columns?size - column_index gt 1>,</#if>
      </#list>
    </foreach>
  </insert>
  
  <#list columns as column>
    <#if column.primary>
  <!-- 根据主键${column.javaName}修改 -->
  <update id="update">
    update `${tableName}`
    <set>
    <#list columns as column>
      <#if !column.primary && !column.unique>
      <#-- 主键或唯一索引不参与修改 -->
        <#if column.javaTypeName == 'java.lang.String'>
        <#-- 字符串 -->
      <if test="${column.javaName} != null and ${column.javaName} != ''"> 
        `${column.dbName}` = ${r"#{"}${column.javaName}${r"}"},
      </if>
        <#else>
        <#-- 普通类型 -->
      <if test="${column.javaName} != null"> 
        `${column.dbName}` = ${r"#{"}${column.javaName}${r"}"},
      </if>
        </#if>
      </#if>
    </#list>
    </set>
    where ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
  </update>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.unique>
  <!-- 根据唯一索引${column.javaName}修改 -->
  <update id="updateBy${column.javaName?cap_first}">
    update `${tableName}`
    <set>
    <#list columns as c>
      <#if !c.primary && !c.unique>
      <#-- 主键或唯一索引不参与修改 -->
        <#if c.javaTypeName == 'java.lang.String'>
        <#-- 字符串 -->
      <if test="${c.javaName} != null"> 
        `${c.dbName}` = ${r"#{"}${c.javaName}${r"}"},
      </if>
        <#else>
        <#-- 普通类型 -->
      <if test="${c.javaName} != null"> 
        `${c.dbName}` = ${r"#{"}${c.javaName}${r"}"},
      </if>
        </#if>
      </#if>
    </#list>
    </set>
    where ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
  </update>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.primary>
  <!-- 根据主键${column.javaName}删除 -->
  <delete id="delete">
    delete from `${tableName}` where ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
  </delete>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.unique>
  <!-- 根据唯一索引${column.javaName}删除 -->
  <delete id="deleteBy${column.javaName?cap_first}">
    delete from `${tableName}` where ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
  </delete>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.primary>
  <!-- 根据主键${column.javaName}数组删除 -->
  <delete id="deleteByArray">
    delete from `${tableName}`
    <where>
      <foreach collection="${column.javaName}s" item="${column.javaName}" separator="or">
        ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
      </foreach>
      </where>
  </delete>
    </#if>
  </#list>
  
  <#list columns as column>
    <#if column.unique>
  <!-- 根据唯一索引${column.javaName}数组删除 -->
  <delete id="deleteBy${column.javaName?cap_first}Array">
    delete from `${tableName}`
    <where>
      <foreach collection="${column.javaName}s" item="${column.javaName}" separator="or">
          ${column.dbName} =  ${r"#{"}${column.javaName}${r"}"}
      </foreach>
      </where>
  </delete>
    </#if>
  </#list>
  
  <!-- 查询列表 -->
  <select id="findList" resultType="${packageName}.base.entity.${javaName}">
    select
      <include refid="columns" />
    from `${tableName}`
    <where>
      <include refid="querys" />
    </where>
  </select>
  
</mapper>