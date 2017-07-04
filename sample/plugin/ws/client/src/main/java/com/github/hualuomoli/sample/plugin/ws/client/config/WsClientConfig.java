package com.github.hualuomoli.sample.plugin.ws.client.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.hualuomoli.sample.plugin.ws.client.service.ServerService;
import com.github.hualuomoli.sample.plugin.ws.client.util.SoapUtils;

@Configuration(value = "com.github.hualuomoli.sample.plugin.ws.client.config.WsClientConfig")
public class WsClientConfig {

  @Bean
  public ServerService wsSms() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("name", "wz");
    map.put("password", "wz12345679");
    Interceptor<SoapMessage> interceptor = SoapUtils.getAuthInterceptor(map, "qname");

    return SoapUtils.getService("http://localhost/ws/serverService?wsdl", ServerService.class, interceptor);
  }

}
