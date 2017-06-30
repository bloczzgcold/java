package com.github.hualuomoli.gateway.client.dealer;

import com.github.hualuomoli.gateway.api.enums.EncryptionEnum;
import com.github.hualuomoli.gateway.api.lang.InvalidDataException;

/**
 * 加解密处理类
 */
public interface EncryptionDealer {

    /**
     * 是否支持
     * @param encryption 加解密类型
     * @return 是否支持
     */
    boolean support(EncryptionEnum encryption);

    /**
     * 加密
     * @param data 待加密内容
     * @param partnerId 合作伙伴ID
     * @return 密文
     */
    String encrypt(String data, String partnerId);

    /**
     * 解密
     * @param data 待解密内容
     * @param partnerId 合作伙伴ID
     * @return 明文
     * @throws InvalidDataException 数据不合法
     */
    String decrypt(String data, String partnerId) throws InvalidDataException;

}
