package com.github.hualuomoli.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.github.hualuomoli.config.mvc.ConvertConfig;
import com.github.hualuomoli.config.mvc.ExceptionConfig;
import com.github.hualuomoli.config.mvc.FileuploadConfig;
import com.github.hualuomoli.config.mvc.InterceptorConfig;
import com.github.hualuomoli.config.mvc.MvcAspectConfig;
import com.github.hualuomoli.config.mvc.MvcComponentConfig;
import com.github.hualuomoli.config.mvc.RequestVersionConfig;
import com.github.hualuomoli.config.mvc.StaticResourceConfig;
import com.github.hualuomoli.config.mvc.ViewConfig;

/**
 * mvc
 */
@Configuration(value = "com.github.hualuomoli.config.MvcConfig")
@Import(value = { RequestVersionConfig.class//
    , ConvertConfig.class //
    , ExceptionConfig.class //
    , FileuploadConfig.class //
    , InterceptorConfig.class //
    , MvcAspectConfig.class //
    , MvcComponentConfig.class//
    , StaticResourceConfig.class //
    , ViewConfig.class //
})
public class MvcConfig {

}
