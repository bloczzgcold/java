package com.github.hualuomoli.creator.reverse.base.mysql;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.hualuomoli.tool.util.TemplateUtils;
import com.google.common.collect.Maps;
import com.github.hualuomoli.creator.reverse.base.MapperBaseCreater;
import com.github.hualuomoli.creator.reverse.component.entity.JavaColumn;

@Service(value = "com.github.hualuomoli.creator.reverse.base.mysql.MysqlMapperBaseCreater")
public class MysqlMapperBaseCreater implements MapperBaseCreater {

  @Override
  public void create(String outputProjectPath, String rootPackageName, String entityName, String entityComment, List<JavaColumn> javaColumns) {
    String outputPath = outputProjectPath + "/src/main/java/" + StringUtils.replace(rootPackageName, ".", "/");

    Map<String, Object> data = Maps.newHashMap();
    data.put("comment", entityComment);
    data.put("packageName", rootPackageName);
    data.put("javaName", entityName);
    data.put("columns", javaColumns);

    TemplateUtils.processByResource("tpl/base/mysql", "mapper.tpl", data, new File(outputPath + "/base/mapper", entityName + "BaseMapper.java"));
  }

}
