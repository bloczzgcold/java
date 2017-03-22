package com.wizhong.ln.validator;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.hualuomoli.validator.Validate;
import com.github.hualuomoli.validator.lang.InvalidParameterException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ValidateTest {

	@Test
	public void test01() {
		A a = new A();
		a.setCode("01");
		Validate.valid(a);
	}

	@Test
	public void test02InnerCheck() {
		A a = new A();
		a.setCode("01");
		B b = new B();
		b.setAge(20);
		a.setB(b);
		Validate.valid(a);
	}

	@Test(expected = InvalidParameterException.class)
	public void test03InnerInvalidCheck() {
		A a = new A();
		a.setCode("01");
		B b = new B();
		b.setAge(null);
		a.setB(b);
		Validate.valid(a);
	}

	@Test
	public void test04InnerClassCheck() {
		A a = new A();
		a.setCode("01");
		A.C c = new A.C();
		c.setUsername("hualuomoli");
		a.setC(c);
		Validate.valid(a);
	}

	@Test(expected = InvalidParameterException.class)
	public void test05InnerClassInvalidCheck() {
		A a = new A();
		a.setCode("01");
		A.C c = new A.C();
		c.setUsername("");
		a.setC(c);
		Validate.valid(a);
	}

	public static class A {

		@NotBlank
		private String code;

		@Valid
		private B b;
		@Valid
		private C c;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}

		public static class C {
			@NotBlank
			private String username;

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}
		}

	}

	public static class B {
		@NotNull
		private Integer age;

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}
	}

}
