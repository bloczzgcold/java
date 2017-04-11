package com.github.hualuomoli.demo.framework.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.hualuomoli.demo.framework.config.BaseConfig;
import com.github.hualuomoli.demo.framework.config.MvcConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({ //
		@ContextConfiguration(name = "parent", classes = BaseConfig.class), //
		@ContextConfiguration(name = "child", classes = MvcConfig.class) //
})
public class ControllerTest {

	// http://www.csdn123.com/html/mycsdn20140110/a7/a75383fcc7d869a7627583ada5e76e46.html
	// perform：执行一个RequestBuilder请求，会自动执行SpringMVC的流程并映射到相应的控制器执行处理；
	// andDo：添加ResultHandler结果处理器，比如调试时打印结果到控制台；
	// andExpect：添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确；
	// andReturn：最后返回相应的MvcResult；然后进行自定义验证/进行下一步的异步处理；

	protected static final Logger logger = LoggerFactory.getLogger(ControllerTest.class);

	@Autowired
	private WebApplicationContext wac;
	protected MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

}
