package com.github.hualuomoli.gateway.android.base64;

public interface Base64 {

  /**
   * 编码
   * @param bytes 字节数据
   * @return 编码后的数据
   */
  String encode(byte[] bytes);

  /**
   * 解码
   * @param data 编码数据
   * @return 解码后的字节
   */
  byte[] decode(String data);

}
