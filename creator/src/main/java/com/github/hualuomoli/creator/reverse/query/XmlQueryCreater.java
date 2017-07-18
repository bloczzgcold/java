package com.github.hualuomoli.creator.reverse.query;

import java.util.List;

import com.github.hualuomoli.creator.reverse.component.entity.JavaColumn;

/**
 * XML生成器
 */
public interface XmlQueryCreater {

  /**
   * 生成mapper的xml
   * @param outputProjectPath 输出项目目录
   * @param rootPackageName 跟包名
   * @param entityName 实体类名称
   * @param javaColumns java列
   * @param tableName 表名
   */
  void create(String outputProjectPath, String rootPackageName, String entityName, List<JavaColumn> javaColumns, String tableName);

}
