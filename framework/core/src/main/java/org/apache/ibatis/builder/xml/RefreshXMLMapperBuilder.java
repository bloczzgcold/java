package org.apache.ibatis.builder.xml;

import java.io.InputStream;
import java.util.Map;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RefreshConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshXMLMapperBuilder extends XMLMapperBuilder {

  public static final Logger logger = LoggerFactory.getLogger(RefreshXMLMapperBuilder.class);

  private String resource;

  public RefreshXMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource, Map<String, XNode> sqlFragments) {
    super(inputStream, configuration, resource, sqlFragments);
    this.resource = resource;
  }

  @Override
  public void parse() {
    super.parse();
  }

  // 刷新
  public void refresh() {
    logger.info("remove resource {} before parse", resource);
    RefreshConfiguration.getInstance().getLoadedResources().remove(this.resource);
    super.parse();
  }

}
