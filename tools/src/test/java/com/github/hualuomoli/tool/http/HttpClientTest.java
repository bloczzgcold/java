package com.github.hualuomoli.tool.http;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.github.hualuomoli.tool.http.HttpClient.Param;
import com.google.common.collect.Lists;

public class HttpClientTest {

	private static User user;

	@BeforeClass
	public static void beforeClass() {
		user = new User();
		User.Info info = new User.Info();
		List<User.Address> addresses = Lists.newArrayList();

		info.setUsercode("hualuomoli");
		info.setUsername("花落寞离");
		info.setAge(20);
		info.setMills(System.currentTimeMillis());
		info.setSallery(2000D);
		info.setDatetime(new Date());

		User.Address address1 = new User.Address();
		address1.provice = "山东省";
		List<User.Address.Phone> phones1 = Lists.newArrayList();
		phones1.add(new User.Address.Phone("15689952699"));
		phones1.add(new User.Address.Phone("18866666666"));
		address1.setPhones(phones1);

		User.Address address2 = new User.Address();
		address2.provice = "北京市";
		List<User.Address.Phone> phones2 = Lists.newArrayList();
		phones2.add(new User.Address.Phone("15689952699"));
		phones2.add(new User.Address.Phone("18866666666"));
		address2.setPhones(phones1);

		addresses.add(address1);
		addresses.add(address2);

		user.setInfo(info);
		user.setAddresses(addresses);

	}

	@Test(expected = IOException.class)
	public void testUrlencoded() throws IOException {
		List<Param> params = Lists.newArrayList();
		params.add(new Param("user", user));

		new HttpURLCleint().urlencoded("http://www.baidu.com", Charset.forName("UTF-8"), params);
	}

	@Test(expected = IOException.class)
	public void testJson() throws IOException {

		new HttpURLCleint().json("http://www.baidu.com", Charset.forName("UTF-8"), JSON.toJSONString(user));
	}

	public static class User {

		private Info info;
		private List<Address> addresses;

		public static class Info {
			private String usercode;
			private String username;
			private Integer age;
			private Long mills;
			private Double sallery;
			private Date datetime;

			public String getUsercode() {
				return usercode;
			}

			public void setUsercode(String usercode) {
				this.usercode = usercode;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public Integer getAge() {
				return age;
			}

			public void setAge(Integer age) {
				this.age = age;
			}

			public Long getMills() {
				return mills;
			}

			public void setMills(Long mills) {
				this.mills = mills;
			}

			public Double getSallery() {
				return sallery;
			}

			public void setSallery(Double sallery) {
				this.sallery = sallery;
			}

			public Date getDatetime() {
				return datetime;
			}

			public void setDatetime(Date datetime) {
				this.datetime = datetime;
			}
		}

		public static class Address {
			private String provice;
			private List<Phone> phones;

			public String getProvice() {
				return provice;
			}

			public void setProvice(String provice) {
				this.provice = provice;
			}

			public List<Phone> getPhones() {
				return phones;
			}

			public void setPhones(List<Phone> phones) {
				this.phones = phones;
			}

			public static class Phone {
				private String number;

				public Phone(String number) {
					super();
					this.number = number;
				}

				public String getNumber() {
					return number;
				}

				public void setNumber(String number) {
					this.number = number;
				}
			}

		}

		public Info getInfo() {
			return info;
		}

		public void setInfo(Info info) {
			this.info = info;
		}

		public List<Address> getAddresses() {
			return addresses;
		}

		public void setAddresses(List<Address> addresses) {
			this.addresses = addresses;
		}
	}

}
