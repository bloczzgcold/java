package com.github.hualuomoli.gateway.android.callback;

/**
 * Object回调
 * @param <T> 回调数据类型
 */
public interface CallbackObject<T> extends Callback {

  /**
   * 业务执行成功
   * @param result 结果
   */
  void onSuccess(T result);

}
