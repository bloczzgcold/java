package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hualuomoli.tool.http.entity.Header;
import com.google.common.collect.Lists;

/**
 * 抽象
 */
public abstract class AbstractHttpCleint implements HttpClient {

    @Override
    public String get(Object object) throws IOException {
        // 请求的内容
        String content = Utils.getRequestContent(object, getCharset(), getFormater());

        List<Header> addRequestHeaders = new ArrayList<Header>();

        // 执行
        return this.execute(content, HttpMethod.GET, addRequestHeaders);
    }

    @Override
    public String get(Map<String, Object> paramMap) throws IOException {
        // 请求的内容
        String content = Utils.getRequestContent(paramMap, getCharset(), getFormater());

        List<Header> addRequestHeaders = new ArrayList<Header>();

        // 执行
        return this.execute(content, HttpMethod.GET, addRequestHeaders);
    }

    @Override
    public String urlencoded(Object object) throws IOException {
        // 请求的内容
        String content = Utils.getRequestContent(object, getCharset(), getFormater());

        // headers(添加Content-Type)
        List<Header> addRequestHeaders = new ArrayList<Header>();
        addRequestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

        // 执行
        return this.execute(content, HttpMethod.POST, addRequestHeaders);
    }

    @Override
    public String urlencoded(Map<String, Object> paramMap) throws IOException {
        // 请求的内容
        String content = Utils.getRequestContent(paramMap, getCharset(), getFormater());

        // headers(添加Content-Type)
        List<Header> addRequestHeaders = new ArrayList<Header>();
        addRequestHeaders.add(new Header("Content-Type", "application/x-www-form-urlencoded"));

        // 执行
        return this.execute(content, HttpMethod.POST, addRequestHeaders);
    }

    @Override
    public String json(String content) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 获取编码集
     * @return 编码集
     */
    protected abstract String getCharset();

    /**
     * 获取格式化工具
     * @return 格式化工具
     */
    protected abstract HttpClient.Formater getFormater();

    /**
     * 执行
     * @param content 请求内容
     * @param httpMethod Http方法
     * @param addRequestHeaders 增加的请求头信息
     * @return 执行结果
     * @throws IOException 处理异常
     */
    protected abstract String execute(String content, HttpMethod httpMethod, List<Header> addRequestHeaders) throws IOException;

    // object tool
    private static class ObjectUtils {

        /**
         * 获取类的所有属性及所有父属性
         * @param clazz 类
         * @return 类的属性及所有父属性
         */
        public static List<Field> getFields(Class<?> clazz) {
            List<Field> fields = ObjectUtils.getFields(clazz, new HashSet<String>());

            return fields;
        }

        /**
         * 获取类的所有属性及所有父属性
         * @param clazz 类
         * @param names 已经存在的属性
         * @return 类的属性及所有父属性
         */
        private static List<Field> getFields(Class<?> clazz, Set<String> names) {
            List<Field> fieldList = new ArrayList<Field>();
            if (clazz == null) {
                return fieldList;
            }
            if (names == null) {
                names = new HashSet<String>();
            }
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (names.contains(field.getName())) {
                    continue;
                }
                if (field.getName().startsWith("this$")) {
                    continue;
                }
                fieldList.add(field);
                names.add(field.getName());
            }
            fieldList.addAll(ObjectUtils.getFields(clazz.getSuperclass(), names));
            return fieldList;
        }

        /**
         * 获取属性值
         * @param field 属性
         * @param obj 数据
         * @param clazz 数据类型
         * @return 值
         */
        public static Object getFieldValue(Field field, Object obj, Class<?> clazz) {

            String name = field.getName();

            try {
                String upper = name.substring(0, 1).toUpperCase();
                if (name.length() > 1) {
                    upper += name.substring(1);
                }
                String methodName = null;
                if (Boolean.class.isAssignableFrom(field.getType()) || boolean.class.isAssignableFrom(field.getType())) {
                    methodName = "is" + upper;
                } else {
                    methodName = "get" + upper;
                }
                Method method = clazz.getMethod(methodName);
                return method.invoke(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // end
        }

    }

    private static class ParamUtils {

        /**
        * 获取请求参数
        * @param object object数据
        * @param formater 格式化
        * @return 请求参数集合
        */
        public static List<RequestParam> getRequestParams(Object object, HttpClient.Formater formater) {

            List<RequestParam> params = Lists.newArrayList();
            if (object == null) {
                return params;
            }

            Class<?> clazz = object.getClass();
            List<Field> fields = ObjectUtils.getFields(clazz);
            for (Field field : fields) {
                String name = field.getName();
                Object value = ObjectUtils.getFieldValue(field, object, clazz);
                params.addAll(ParamUtils.getRequestParams(name, value, field, formater));
            }

            return params;
        }

        /**
         * 获取urlencoded放肆请求的参数
         * @param map 参数
         * @param dateFormat 日期格式化方式
         * @return 参数信息
         */
        public static List<RequestParam> getRequestParams(Map<String, Object> map, HttpClient.Formater formater) {

            List<RequestParam> params = new ArrayList<RequestParam>();
            if (map == null || map.size() == 0) {
                return params;
            }

            for (String name : map.keySet()) {
                params.addAll(ParamUtils.getRequestParams(name, map.get(name), null, formater));
            }

            return params;
        }

        @SuppressWarnings("unchecked")
        private static List<RequestParam> getRequestParams(String name, Object value, Field f, HttpClient.Formater formater) {

            List<RequestParam> params = Lists.newArrayList();

            if (value == null) {
                return params;
            }

            Class<?> clazz = value.getClass();

            // 字符串
            if (String.class.isAssignableFrom(clazz)) {
                params.add(new RequestParam(name, String.valueOf(value)));
                return params;
            }

            // 数值
            if (Integer.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)//
                    || Float.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
                params.add(new RequestParam(name, String.valueOf(value)));
                return params;
            }

            // 日期
            if (Date.class.isAssignableFrom(clazz)) {
                params.add(new RequestParam(name, formater.format((Date) value, f)));
                return params;
            }

            // 枚举
            if (Enum.class.isAssignableFrom(clazz)) {
                params.add(new RequestParam(name, ((Enum<?>) value).name()));
                return params;
            }

            // map
            if (Map.class.isAssignableFrom(clazz)) {
                Map<Object, Object> map = (Map<Object, Object>) value;

                for (Object key : map.keySet()) {
                    if (key == null) {
                        continue;
                    }
                    String k = key.toString();
                    Object v = map.get(key);
                    // name[attribute]
                    params.addAll(ParamUtils.getRequestParams(name + "." + k, v, f, formater));
                }
                return params;
            }

            // list
            if (List.class.isAssignableFrom(clazz)) {
                List<?> list = (List<?>) value;

                for (int i = 0; i < list.size(); i++) {
                    Object v = list.get(i);
                    // name[index]
                    params.addAll(ParamUtils.getRequestParams(name + "[" + i + "]", v, f, formater));
                }
                return params;
            }

            // 实体类
            List<Field> fields = ObjectUtils.getFields(clazz, new HashSet<String>());

            for (Field field : fields) {
                String fieldName = field.getName();
                Object v = ObjectUtils.getFieldValue(field, value, clazz);
                params.addAll(ParamUtils.getRequestParams(name + "." + fieldName, v, field, formater));
            }
            return params;
        }

    }

    // 排序
    private static class Utils {

        /**
         * 排序
         * @param list 集合
         * @return 排序后的集合
         */
        public static List<RequestParam> sort(List<RequestParam> list) {

            if (list == null || list.size() == 0) {
                return list;
            }

            Collections.sort(list, new Comparator<RequestParam>() {

                @Override
                public int compare(RequestParam o1, RequestParam o2) {
                    String name1 = o1.name;
                    String name2 = o2.name;

                    int len1 = name1.length();
                    int len2 = name2.length();

                    int len = len1 > len2 ? len2 : len1;

                    for (int i = 0; i < len; i++) {
                        int c = name1.charAt(i) - name2.charAt(i);

                        if (c == 0) {
                            continue;
                        }

                        return c;

                    }

                    return len1 - len2;
                }
            });

            return list;
        }

        /**
         * 对值进行编码
         * @param value 值
         * @param charset 编码类型
         * @return 编码后的值
         */
        public static String encoded(String value, String charset) {
            if (value == null) {
                return null;
            }

            try {
                return URLEncoder.encode(value, charset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * 获取请求的内容
         * @param object 数据
         * @param charset 数据编码集
         * @param formater 格式化
         * @return 请求的内容
         */
        public static String getRequestContent(Object object, String charset, HttpClient.Formater formater) {
            List<RequestParam> paramList = ParamUtils.getRequestParams(object, formater);

            return getRequestContent(paramList, charset, formater);
        }

        /**
         * 获取请求的内容
         * @param map 数据
         * @param charset 数据编码集
         * @param formater 格式化
         * @return 请求的内容
         */
        public static String getRequestContent(Map<String, Object> paramMap, String charset, HttpClient.Formater formater) {
            List<RequestParam> paramList = ParamUtils.getRequestParams(paramMap, formater);

            return getRequestContent(paramList, charset, formater);
        }

        /**
         * 获取请求的内容
         * @param paramList 请求参数
         * @param charset 数据编码集
         * @param formater 格式化
         * @return
         */
        private static String getRequestContent(List<RequestParam> paramList, String charset, HttpClient.Formater formater) {
            if (paramList == null || paramList.size() == 0) {
                return StringUtils.EMPTY;
            }

            // 排序
            Utils.sort(paramList);

            StringBuilder buffer = new StringBuilder();

            for (RequestParam param : paramList) {
                buffer.append("&").append(param.name).append("=").append(Utils.encoded(param.value, charset));
            }

            return buffer.substring(1).toString();
        }

    }

    // 参数
    private static class RequestParam {

        public String name;
        public String value;

        public RequestParam(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

}
