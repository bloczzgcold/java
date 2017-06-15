package com.github.hualuomoli.tool.http.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * 头信息
 */
public class Header {

    /** 名称 */
    private String name;
    /** 值 */
    private String[] value;

    public Header(String name, String... value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String[] getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!Header.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        Header header = (Header) obj;

        return StringUtils.equals(header.name, this.name);
    }

    @Override
    public String toString() {
        return "Header [name=" + name + ", value=" + value + "]";
    }

}
