package com.github.hualuomoli.sample.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.sample.framework.config.mvc.ConvertConfig;
import com.github.hualuomoli.sample.framework.config.mvc.ExceptionConfig;
import com.github.hualuomoli.sample.framework.config.mvc.FileuploadConfig;
import com.github.hualuomoli.sample.framework.config.mvc.InterceptorConfig;
import com.github.hualuomoli.sample.framework.config.mvc.MvcAspectConfig;
import com.github.hualuomoli.sample.framework.config.mvc.MvcComponentConfig;
import com.github.hualuomoli.sample.framework.config.mvc.StaticResourceConfig;
import com.github.hualuomoli.sample.framework.config.mvc.ViewConfig;

/**
 * mvc
 */
@Configuration
@Import(value = { ConvertConfig.class //
    , ExceptionConfig.class //
    , FileuploadConfig.class //
    , InterceptorConfig.class //
    , MvcAspectConfig.class //
    , MvcComponentConfig.class//
    , StaticResourceConfig.class //
    , ViewConfig.class })
public class MvcConfig {

}
