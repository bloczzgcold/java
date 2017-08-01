package com.github.hualuomoli.gateway.android.callback;

import java.util.List;

/**
 * Array数据回调
 * @param <T> 回调数据
 */
public interface CallbackArray<T> extends Callback {

  /**
   * 业务执行成功
   * @param results 数据集合
   */
  void onSuccess(List<T> results);

}
