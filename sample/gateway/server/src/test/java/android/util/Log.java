package android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

  private static final Logger logger = LoggerFactory.getLogger(Log.class);

  Log() {
  }

  public static int v(java.lang.String tag, java.lang.String msg) {
    logger.debug("[{}] {}", tag, msg);
    return 0;
  }

  public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
    logger.debug("[{}] {}", tag, msg, tr);
    return 0;
  }

  public static int d(java.lang.String tag, java.lang.String msg) {
    logger.debug("[{}] {}", tag, msg);
    return 0;
  }

  public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
    logger.debug("[{}] {}", tag, msg, tr);
    return 0;
  }

  public static int i(java.lang.String tag, java.lang.String msg) {
    logger.info("[{}] {}", tag, msg);
    return 0;
  }

  public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
    logger.info("[{}] {}", tag, msg, tr);
    return 0;
  }

  public static int w(java.lang.String tag, java.lang.String msg) {
    logger.warn("[{}] {}", tag, msg);
    return 0;
  }

  public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
    logger.warn("[{}] {}", tag, msg, tr);
    return 0;
  }

  public static native boolean isLoggable(java.lang.String tag, int level);

  public static int w(java.lang.String tag, java.lang.Throwable tr) {
    logger.warn("[{}] {}", tag, tr);
    return 0;
  }

  public static int e(java.lang.String tag, java.lang.String msg) {
    logger.error("[{}] {}", tag, msg);
    return 0;
  }

  public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
    logger.error("[{}] {}", tag, msg, tr);
    return 0;
  }

  public static final int VERBOSE = 2;
  public static final int DEBUG = 3;
  public static final int INFO = 4;
  public static final int WARN = 5;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;
}
