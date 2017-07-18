package com.github.hualuomoli.framework.mvc.version.condition;

import javax.servlet.http.HttpServletRequest;

final class DefaultParser implements Parser {

  @Override
  public String getVersion(HttpServletRequest req) {
    String version = req.getHeader("api-version");
    if (version == null || version.trim().length() == 0) {
      return null;
    }
    return version.trim();
  }

  @Override
  public int compare(String v1, String v2) {

    if (v1 == null && v2 == null) {
      return 0;
    }
    if (v1 == null) {
      return -1;
    }
    if (v2 == null) {
      return 1;
    }

    if (v1.equals(v2)) {
      return 0;
    }

    String[] array1 = v1.split("[.]");
    String[] array2 = v2.split("[.]");
    int len1 = array1.length;
    int len2 = array2.length;

    int length = len1 > len2 ? len2 : len1;
    for (int i = 0; i < length; i++) {
      int value1 = Integer.parseInt(array1[i]);
      int value2 = Integer.parseInt(array2[i]);

      // 如果相等,继续下一个比较
      int c = value1 - value2;
      if (c == 0) {
        continue;
      }

      return c;
    }

    return len1 - len2;

  }

}
