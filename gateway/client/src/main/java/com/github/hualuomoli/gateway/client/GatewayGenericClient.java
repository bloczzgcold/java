package com.github.hualuomoli.gateway.client;

import java.util.List;
import java.util.Map;

import com.github.hualuomoli.gateway.api.lang.BusinessException;
import com.github.hualuomoli.gateway.client.Parser.PageName;
import com.github.hualuomoli.gateway.client.entity.Page;
import com.github.hualuomoli.gateway.client.lang.ClientException;

public class GatewayGenericClient extends GatewayClient {

    private Parser parser;

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public <T> T callObject(Object req, Class<T> clazz) throws BusinessException, ClientException {
        String method = null;

        if (List.class.isAssignableFrom(req.getClass())) {
            List<?> list = (List<?>) req;
            method = parser.method(list.get(0));
        } else {
            method = parser.method(req);
        }

        String result = this.call(method, jsonParser.toJsonString(req));

        return jsonParser.parseObject(result, clazz);
    }

    public <T> List<T> callArray(Object req, Class<T> clazz) throws BusinessException, ClientException {
        String method = null;

        if (List.class.isAssignableFrom(req.getClass())) {
            List<?> list = (List<?>) req;
            method = parser.method(list.get(0));
        } else {
            method = parser.method(req);
        }

        String result = this.call(method, jsonParser.toJsonString(req));

        return jsonParser.parseArray(result, clazz);
    }

    public <T> Page<T> callPage(Object req, Class<T> clazz) throws BusinessException, ClientException {
        String method = null;

        if (List.class.isAssignableFrom(req.getClass())) {
            List<?> list = (List<?>) req;
            method = parser.method(list.get(0));
        } else {
            method = parser.method(req);
        }

        String result = this.call(method, jsonParser.toJsonString(req));

        // page name
        PageName names = parser.pageName();

        Map<String, Object> map = jsonParser.parse(result);
        Integer pageNumber = Integer.parseInt(String.valueOf(map.get(names.pageNumber())));
        Integer pageSize = Integer.parseInt(String.valueOf(map.get(names.pageSize())));
        Integer count = Integer.parseInt(String.valueOf(map.get(names.count())));

        List<T> dataList = jsonParser.parseArray(jsonParser.toJsonString(map.get(names.datas())), clazz);
        Page<T> page = new Page<T>();
        page.setPageNumber(pageNumber);
        page.setPageSize(pageSize);
        page.setCount(count);
        page.setDataList(dataList);
        return page;
    }

}
