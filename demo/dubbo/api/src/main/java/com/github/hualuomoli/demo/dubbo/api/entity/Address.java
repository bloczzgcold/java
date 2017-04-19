package com.github.hualuomoli.demo.dubbo.api.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable {

	private String areaCode;
	private String provinceName;
	private String cityName;
	private String countyName;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	@Override
	public String toString() {
		return "Address [areaCode=" + areaCode + ", provinceName=" + provinceName + ", cityName=" + cityName + ", countyName=" + countyName + "]";
	}

}
