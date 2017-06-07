package com.github.hualuomoli.tool.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvUtilsTest {

    private static final Logger logger = LoggerFactory.getLogger(EnvUtils.class);

    @BeforeClass
    public static void beforeClass() {
        System.setProperty("environment", EnvUtils.Env.TEST.name());
    }

    @Test
    public void testParse() {
        String[] resources = null;

        // logs/log4j.properties
        resources = EnvUtils.parse("logs/log4j.properties");
        for (String resource : resources) {
            logger.debug("1. resource={}", resource);
        }

        // /logs/log4j.properties
        resources = EnvUtils.parse("/logs/log4j.properties");
        for (String resource : resources) {
            logger.debug("2. resource={}", resource);
        }

        // log4j.properties
        resources = EnvUtils.parse("log4j.properties");
        for (String resource : resources) {
            logger.debug("3. resource={}", resource);
        }

        // /log4j.properties
        resources = EnvUtils.parse("/log4j.properties");
        for (String resource : resources) {
            logger.debug("4. resource={}", resource);
        }

        // logs/log4j
        resources = EnvUtils.parse("logs/log4j");
        for (String resource : resources) {
            logger.debug("5. resource={}", resource);
        }

        // log4j
        resources = EnvUtils.parse("log4j");
        for (String resource : resources) {
            logger.debug("6. resource={}", resource);
        }

    }

    @Test
    public void testGetEnv() {
        logger.debug("env={}", EnvUtils.getEnv());
    }

}
