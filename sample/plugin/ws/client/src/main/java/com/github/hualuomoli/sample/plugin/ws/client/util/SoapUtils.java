package com.github.hualuomoli.sample.plugin.ws.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.github.hualuomoli.sample.plugin.ws.client.lang.SoapException;

public class SoapUtils {

  /**
   * 获取服务
   * @param address wsdl地址
   * @param clazz 服务类型
   * @return 服务
   * @throws SoapException 获取服务错误
   */
  public static <T> T getService(String address, Class<T> clazz) throws SoapException {
    return getService(address, clazz, getAuthInterceptor());
  }

  /**
   * 获取服务
   * @param address wsdl地址
   * @param clazz 服务类型
   * @param interceptor 拦截器
   * @return 服务
   * @throws SoapException 获取服务错误
   */
  public static <T> T getService(String address, Class<T> clazz, Interceptor<SoapMessage> interceptor) throws SoapException {

    try {
      JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
      factory.setServiceClass(clazz);
      factory.setAddress(address);

      // 添加输出 Interceptor
      if (interceptor != null) {
        List<Interceptor<? extends Message>> outInterceptors = new ArrayList<Interceptor<? extends Message>>();
        outInterceptors.add(interceptor);
        factory.setOutInterceptors(outInterceptors);
      }

      return factory.create(clazz);
    } catch (Exception e) {
      throw new SoapException(e);
    }

  }

  /**
   * 获取权限拦截器
   * @return 权限拦截器
   */
  public static Interceptor<SoapMessage> getAuthInterceptor() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("name", "admin");
    map.put("password", "admin123456");

    return getAuthInterceptor(map, "qname");
  }

  /**
   * 获取权限拦截器
   * @return 权限拦截器
   */
  public static Interceptor<SoapMessage> getAuthInterceptor(final Map<String, String> map, final String qname) {
    return new AbstractPhaseInterceptor<SoapMessage>(Phase.WRITE) {

      @Override
      public void handleMessage(SoapMessage soapmessage) throws Fault {
        final Document document = DOMUtils.createDocument();

        // root
        final Element root = document.createElementNS(qname, "auth");

        // add element
        for (String key : map.keySet()) {
          final Element e = document.createElement(key);
          e.setTextContent(map.get(key));
          root.appendChild(e);
        }

        soapmessage.getHeaders().add(new SoapHeader(new QName(""), root));
      }

    };
  }

}
