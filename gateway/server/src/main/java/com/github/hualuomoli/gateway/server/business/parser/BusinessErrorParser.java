package com.github.hualuomoli.gateway.server.business.parser;

import com.github.hualuomoli.gateway.api.lang.BusinessException;

/**
 * 解析器
 */
public interface BusinessErrorParser {

  /**
   * 解析错误
   * @param e 不合法的访问权限
   * @return #BusinessException
   */
  BusinessException parse(IllegalAccessException e);

  /**
   * 解析错误
   * @param e 参数不合法
   * @return #BusinessException
   */
  BusinessException parse(IllegalArgumentException e);

  /**
   * 解析业务执行过程中出现的错误
   * @param t 执行过程中的错误
   * @return #BusinessException
   */
  BusinessException parse(Throwable t);

}
