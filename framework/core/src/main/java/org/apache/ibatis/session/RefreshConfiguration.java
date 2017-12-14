package org.apache.ibatis.session;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.parsing.XNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefreshConfiguration extends Configuration {

  private static final Logger logger = LoggerFactory.getLogger(RefreshConfiguration.class);

  public static Boolean inited = false;
  protected final Map<String, XNode> sqlFragments = new RefreshStrictMap<XNode>("XML fragments parsed from previous mappers");

  private static final RefreshConfiguration CONFIGURATION = new RefreshConfiguration();

  public static RefreshConfiguration getInstance() {
    return CONFIGURATION;
  }

  private RefreshConfiguration() {
    super();
  }

  public Set<String> getLoadedResources() {
    return super.loadedResources;
  }

  @Override
  public Map<String, XNode> getSqlFragments() {
    return sqlFragments;
  }

  @Override
  public void addMappedStatement(MappedStatement ms) {
    if (inited) {
      super.mappedStatements.remove(ms.getId());
    }
    super.addMappedStatement(ms);
  }

  @Override
  public void addResultMap(ResultMap rm) {
    if (inited) {
      super.resultMaps.remove(rm.getId());
    }
    super.addResultMap(rm);
  }

  @Override
  public void addParameterMap(ParameterMap pm) {
    if (inited) {
      super.parameterMaps.remove(pm.getId());
    }
    super.addParameterMap(pm);
  }

  @Override
  public void addCache(Cache cache) {
    if (inited) {
      super.caches.remove(cache.getId());
    }
    super.addCache(cache);
  }

  @Override
  public void addKeyGenerator(String id, KeyGenerator keyGenerator) {
    if (inited) {
      super.keyGenerators.remove(id);
    }
    super.addKeyGenerator(id, keyGenerator);
  }

  @SuppressWarnings("serial")
  public static class RefreshStrictMap<V> extends Configuration.StrictMap<V> {

    public RefreshStrictMap(String name) {
      super(name);
    }

    @Override
    public V put(String key, V value) {
      if (inited) {
        logger.debug("refresh id {}", key);
        super.remove(key);
      }
      return super.put(key, value);
    }

  }
  // end inner class
}
