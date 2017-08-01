package com.github.hualuomoli.gateway.android.callback;

/**
 * 字符串回调
 */
public interface CallbackString extends Callback {

  /**
   * 业务处理成功
   * @param result 响应业务数据
   */
  void onSuccess(String result);

}
