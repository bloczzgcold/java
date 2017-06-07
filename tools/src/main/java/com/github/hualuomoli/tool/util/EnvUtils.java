package com.github.hualuomoli.tool.util;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Lists;

public class EnvUtils {

    private static final Lock LOCK = new ReentrantLock();
    private static String[] keys = new String[] { "environment", "env" };
    private static Env env = null;

    /**
     * 初始化环境变量的名称,默认为environment,env
     * @param keys 名称
     */
    public static final void init(String... keys) {
        EnvUtils.keys = keys;
    }

    /**
     * 转换资源路径
     * @param resources 资源文件
     * @return 包含运行环境的资源文件 
     */
    public static final String[] parse(String... resources) {
        List<String> list = Lists.newArrayList();
        for (String resource : resources) {
            list.addAll(parse2List(resource));
        }
        return list.toArray(new String[] {});
    }

    /**
     * 转换资源路径logs/log4j.properties将返回如下信息(假设环境是test)
     * logs/test/log4j.properties           当前文件目录下的test目录下的配置文件
     * logs-test/log4j.properties           当前文件父目录加-test
     * logs/log4j-test.properties           当前文件名增加-test
     * logs/log4j.properties                当前文件
     * 
     * @param resource 资源文件
     * @return 包含运行环境的资源文件 
     */
    public static final String[] parse(String resource) {
        return parse2List(resource).toArray(new String[] {});
    }

    /**
     * 转换资源路径logs/log4j.properties将返回如下信息(假设环境是test)
     * logs/log4j.properties                当前文件
     * logs/log4j-test.properties           当前文件名增加-test
     * logs-test/log4j.properties           当前文件父目录加-test
     * logs/test/log4j.properties           当前文件目录下的test目录下的配置文件
     * @param resource 资源文件
     * @return 包含运行环境的资源文件 
     * @return
     */
    private static final List<String> parse2List(String resource) {
        if (resource == null || resource.length() == 0) {
            return Lists.newArrayList(resource);
        }
        List<String> resources = Lists.newArrayList();
        String name = getEnv().name().toLowerCase();
        resource = resource.replaceAll("\\\\", "/");
        int folderLastIndex = resource.lastIndexOf("/");

        // 原文件
        resources.add(resource);

        // 文件增加运行环境
        int fileLastIndex = resource.lastIndexOf(".");
        if (fileLastIndex > 0) {
            resources.add(resource.substring(0, fileLastIndex) + "-" + name //
                    + "." + resource.substring(fileLastIndex + 1)); // logs/log4j-test.properties
        }

        // 增加文件的文件夹
        if (folderLastIndex > 0) {
            String folder = resource.substring(0, folderLastIndex); // logs
            String filename = resource.substring(folderLastIndex + 1); // log4j.properties

            resources.add(folder + "-" + name + "/" + filename); // logs-test/log4j.properties
            resources.add(folder + "/" + name + "/" + filename); // logs/test/log4j.properties

        }

        return resources;
    }

    /**
     * 获取环境变量
     * @return 环境变量
     */
    public static final Env getEnv() {

        if (env != null) {
            return env;
        }

        try {
            // lock
            LOCK.lock();

            if (env != null) {
                return env;
            }

            String runtime = getRuntime(keys);
            System.out.println("runtime=" + runtime);

            if (runtime != null && runtime.length() > 0) {
                env = Env.valueOf(Env.class, runtime.toUpperCase());
            } else {
                env = Env.PRODUCT;
            }
        } finally {
            System.out.println("运行环境env=" + env.name().toLowerCase());
            // unlock
            LOCK.unlock();
        }

        return env;

    }

    /**
     * 获取运行环境
     * @param keys 参数名称
     * @return 参数值
     */
    private static final String getRuntime(String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }

        String value = null;

        for (String key : keys) {
            value = getRuntime(key);
            if (value != null && value.trim().length() > 0) {
                break;
            }
        }

        return value;
    }

    /**
     * 获取运行环境
     * 1、获取-D的参数
     * 2、获取export的环境变量
     * @param key 参数名称
     * @return 参数值
     */
    private static final String getRuntime(String key) {
        String value = null;

        // read from -D parameter. 
        // such as java -Denv=test Main
        if (value == null || value.trim().length() == 0) {
            value = System.getProperty(key);
        }

        // read from environment configure.
        // such as export env=test & java Main
        if (value == null || value.trim().length() == 0) {
            value = System.getenv(key);
        }

        return value;
    }

    // 运行环境
    public static enum Env {
        /** 开发环境 */
        DEV,
        /** 测试环境 */
        TEST,
        /** 集成测试 */
        INTEGRATE,
        /** 准生产环境 */
        PREPARE,
        /** 生产环境 */
        PRODUCT;
    }

}
