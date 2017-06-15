package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public interface HttpClient {

    /**
     * 执行http请求(get)
     * @param object 请求参数
     * @return 执行结果
     * @throws IOException 执行错误
     */
    String get(Object object) throws IOException;

    /**
     * 执行http请求(get)
     * @param paramMap 请求参数
     * @return 执行结果
     * @throws IOException 执行错误
     */
    String get(Map<String, Object> paramMap) throws IOException;

    /**
     * 执行http请求(urlencoded)
     * @param object 请求参数
     * @return 执行结果
     * @throws IOException 执行错误
     */
    String urlencoded(Object object) throws IOException;

    /**
     * 执行http请求(urlencoded)
     * @param paramMap 请求参数
     * @return 执行结果
     * @throws IOException 执行错误
     */
    String urlencoded(Map<String, Object> paramMap) throws IOException;

    /**
     * 执行http请求(json)
     * @param content 请求内容
     * @return 执行结果
     * @throws IOException 执行错误
     */
    String json(String content) throws IOException;

    // 调用方式
    enum HttpMethod {
        /** POST */
        POST,
        /** GET */
        GET;
    }

    // 格式化
    interface Formater {

        /**
         * 格式化日期
         * @param date 日期
         * @param field 实体类属性
         * @return 日期字符串
         */
        String format(Date date, Field field);

    }

}
