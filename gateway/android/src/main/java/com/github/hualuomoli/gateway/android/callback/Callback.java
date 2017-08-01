package com.github.hualuomoli.gateway.android.callback;

import com.github.hualuomoli.gateway.android.enums.ErrorTypeEnum;

/**
 * 回调
 */
public interface Callback {

  /**
   * 调用失败,该方法正常情况仅出现在调试阶段
   * @param errorType 错误类型
   * @param message 错误信息
   */
  void onError(ErrorTypeEnum errorType, String message);

  /**
   * 业务处理失败
   * @param subCode 业务编码
   * @param subMessage 业务描述
   * @param subErrorCode 失败编码
   */
  void onBusinessError(String subCode, String subMessage, String subErrorCode);

}
