package com.wizhong.ln.validator;

import org.junit.Test;

import com.github.hualuomoli.validator.Validate;
import com.github.hualuomoli.validator.constraints.Greater;
import com.github.hualuomoli.validator.lang.InvalidParameterException;

public class GreaterTest {

	@Test
	public void test() {
		B b = new B();
		b.age = 30;
		Validate.valid(b);
	}

	@Test(expected = InvalidParameterException.class)
	public void testEqual() {
		B b = new B();
		b.age = 20;
		 Validate.valid(b);
	}

	@Test(expected = InvalidParameterException.class)
	public void testLess() {
		B b = new B();
		b.age = 18;
		Validate.valid(b);
	}

	@Test
	public void testNull() {
		B b = new B();
		b.age = null;
		Validate.valid(b);
	}

	public static class B {

		@Greater(value = 20, message = "年龄不能小于20")
		private Integer age;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

	}

}
