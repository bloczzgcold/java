package com.github.hualuomoli.creator.reverse.component.parser;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.github.hualuomoli.creator.reverse.component.entity.DBColumn;
import com.github.hualuomoli.creator.reverse.component.entity.JavaColumn;

public abstract class AbstractParser implements Parser {

  @Override
  public List<JavaColumn> parse(List<DBColumn> dbColumns, String primaryKey, List<String> uniques, Resolver resolver) {
    List<JavaColumn> javaColumns = Lists.newArrayList();

    for (DBColumn dbColumn : dbColumns) {
      javaColumns.add(this.format(dbColumn, resolver));
    }

    // 设置主键
    for (JavaColumn javaColumn : javaColumns) {
      if (StringUtils.equals(javaColumn.getDbName(), primaryKey)) {
        javaColumn.setPrimary(true);
        break;
      }
    }

    for (String unique : uniques) {
      for (JavaColumn javaColumn : javaColumns) {
        if (StringUtils.equals(javaColumn.getDbName(), unique)) {
          javaColumn.setUnique(true);
          break;
        }
      }
    }

    return javaColumns;
  }

  /**
   * 解析
   * @param dbColumn 数据库列
   * @param resolver 解析器
   * @return java列
   */
  private JavaColumn format(DBColumn dbColumn, Resolver resolver) {
    String dbName = dbColumn.getColumnName();
    String javaName = this.getName(dbName);
    String comment = dbColumn.getComment();

    Class<?> javaType = this.formatJavaType(dbColumn);
    if (resolver != null) {
      javaType = resolver.resolverJavaType(javaType, javaName, dbName);
    }

    return new JavaColumn(dbName, javaName, javaType, comment);
  }

  /**
   * 解析java类型
   * @param dbColumn 数据库列信息
   * @return java类型
   */
  protected abstract Class<?> formatJavaType(DBColumn dbColumn);

  protected String getName(String dbName) {
    StringBuilder buffer = new StringBuilder();
    char[] array = dbName.toCharArray();
    boolean upper = false;
    for (char c : array) {
      if (c == '_') {
        upper = true;
        continue;
      }
      if (upper) {
        buffer.append(String.valueOf(c).toUpperCase());
        upper = false;
      } else {
        buffer.append(String.valueOf(c));
      }
    }
    return buffer.toString();
  }

}
