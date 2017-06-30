package com.github.hualuomoli.gateway.client;

public interface Parser {

    /**
     * 获取请求实体类的方法
     * @param object 请求实体类
     * @return 方法
     */
    String method(Object object);

    /**
     * 分页名称信息
     * @return 分页名称
     */
    PageName pageName();

    // 分页名称
    interface PageName {

        String pageNumber();

        String pageSize();

        String count();

        String datas();

    }

}
