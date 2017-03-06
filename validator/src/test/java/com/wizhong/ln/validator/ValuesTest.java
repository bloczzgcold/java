package com.wizhong.ln.validator;

import org.junit.Test;

import com.github.hualuomoli.validator.Validate;
import com.github.hualuomoli.validator.constraints.Values;


public class ValuesTest {

	@Test
	public void test() {
		A a = new A();
		a.week = "Monday";
		Validate.valid(a);
	}

	@Test
	public void testInvalid() {
		A a = new A();
		a.week = "a";
		Validate.valid(a);
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
