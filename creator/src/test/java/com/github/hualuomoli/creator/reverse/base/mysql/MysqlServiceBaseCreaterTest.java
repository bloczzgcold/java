package com.github.hualuomoli.creator.reverse.base.mysql;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.hualuomoli.creator.ServiceTest;
import com.github.hualuomoli.creator.reverse.component.entity.DBColumn;
import com.github.hualuomoli.creator.reverse.component.entity.JavaColumn;
import com.github.hualuomoli.creator.reverse.component.mysql.parser.MysqlParser;
import com.github.hualuomoli.creator.reverse.component.mysql.service.MysqlDBService;

public class MysqlServiceBaseCreaterTest extends ServiceTest {

  @Autowired
  private MysqlDBService dbService;
  @Autowired
  private MysqlParser parser;

  @Autowired
  private MysqlServiceBaseCreater serviceBaseCreater;

  @Test
  public void testCreate() {
    List<DBColumn> dbColumns = dbService.findList(DATABASE_NAME, TABLE_NAME);
    String comments = dbService.findComment(DATABASE_NAME, TABLE_NAME);

    String primaryKey = dbService.findPrimaryKey(DATABASE_NAME, TABLE_NAME);
    List<String> uniques = dbService.findUniqueKey(DATABASE_NAME, TABLE_NAME);

    List<JavaColumn> javaColumns = parser.parse(dbColumns, primaryKey, uniques, null);

    serviceBaseCreater.create(outputPath, packageName, ENTITY_NAME, comments, javaColumns);

    logger.info("service created.");
  }

}
