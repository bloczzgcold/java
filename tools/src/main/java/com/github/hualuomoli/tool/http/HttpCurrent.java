package com.github.hualuomoli.tool.http;

import java.util.ArrayList;
import java.util.List;

import com.github.hualuomoli.tool.http.entity.Header;

public class HttpCurrent {

    private static final ThreadLocal<List<Header>> REQ_HEADERS = new ThreadLocal<List<Header>>();
    private static final ThreadLocal<List<Header>> RES_HEADERS = new ThreadLocal<List<Header>>();

    public static List<Header> getReqHeaders() {
        List<Header> headers = REQ_HEADERS.get();
        if (headers == null) {
            headers = new ArrayList<Header>();
            setReqHeaders(headers);
        }
        return headers;
    }

    public static void setReqHeaders(List<Header> headers) {
        REQ_HEADERS.set(headers);
    }

    public static List<Header> getResHeaders() {
        List<Header> headers = RES_HEADERS.get();
        if (headers == null) {
            headers = new ArrayList<Header>();
        }
        return headers;
    }

    public static void setResHeaders(List<Header> headers) {
        RES_HEADERS.set(headers);
    }

}
