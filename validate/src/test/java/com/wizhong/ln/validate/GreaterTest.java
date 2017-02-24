package com.wizhong.ln.validate;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.hualuomoli.validate.numerical.Greater;
import com.github.hualuomoli.validate.util.ValidatorUtils;

public class GreaterTest {

	@Test
	public void test() {
		B b = new B();
		b.age = 30;
		Set<String> errors = ValidatorUtils.valid(b);
		Assert.assertNull(errors);

	}

	@Test
	public void testEqual() {
		B b = new B();
		b.age = 20;
		Set<String> errors = ValidatorUtils.valid(b);
		Assert.assertNotNull(errors);
		Assert.assertEquals("年龄不能小于20", StringUtils.join(errors, ","));

	}

	@Test
	public void testLess() {
		B b = new B();
		b.age = 18;
		Set<String> errors = ValidatorUtils.valid(b);
		Assert.assertNotNull(errors);
		Assert.assertEquals("年龄不能小于20", StringUtils.join(errors, ","));

	}

	@Test
	public void testNull() {
		B b = new B();
		b.age = null;
		Set<String> errors = ValidatorUtils.valid(b);
		Assert.assertNull(errors);

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
