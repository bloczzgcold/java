package com.wizhong.ln.validate;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.hualuomoli.validate.util.ValidatorUtils;
import com.github.hualuomoli.validate.values.Values;

public class ValuesTest {

	@Test
	public void test() {
		A a = new A();
		a.week = "Monday";

		Set<String> errors = ValidatorUtils.valid(a);
		Assert.assertNull(errors);
	}

	@Test
	public void testInvalid() {
		A a = new A();
		a.week = "a";

		Set<String> errors = ValidatorUtils.valid(a);
		Assert.assertNotNull(errors);
		Assert.assertEquals("不合法的星期", StringUtils.join(errors, ","));
	}

	public static class A {

		@Values(value = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" }, message = "不合法的星期")
		private String week;

		public String getWeek() {
			return week;
		}

		public void setWeek(String week) {
			this.week = week;
		}
	}

}
