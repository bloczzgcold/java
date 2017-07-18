package com.github.hualuomoli.creator.reverse.base.mysql;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.github.hualuomoli.tool.util.TemplateUtils;
import com.google.common.collect.Maps;
import com.github.hualuomoli.creator.reverse.base.XmlBaseCreater;
import com.github.hualuomoli.creator.reverse.component.entity.JavaColumn;

@Component(value = "com.github.hualuomoli.creator.reverse.base.mysql.MysqlXmlBaseCreater")
public class MysqlXmlBaseCreater implements XmlBaseCreater {

  @Override
  public void create(String outputProjectPath, String rootPackageName, String entityName, List<JavaColumn> javaColumns, String tableName) {
    String outputPath = outputProjectPath + "/src/main/resources/mappers/" + StringUtils.replace(rootPackageName, ".", "/");

    Map<String, Object> data = Maps.newHashMap();
    data.put("packageName", rootPackageName);
    data.put("javaName", entityName);
    data.put("columns", javaColumns);
    data.put("tableName", tableName);

    TemplateUtils.processByResource("tpl/base/mysql", "xml.tpl", data, new File(outputPath + "/base", entityName + "BaseMapper.xml"));
  }

}
