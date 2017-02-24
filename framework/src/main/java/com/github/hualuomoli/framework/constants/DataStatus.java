package com.github.hualuomoli.framework.constants;

/**
 * 数据状态
 * @author lbq
 *
 */
public enum DataStatus {

	/** 正常 */
	NOMAL(1, "正常")
	/** 已删除 */
	,DELETED(9, "已删除");

	/** 值 */
	private Integer value;
	/** 描述 */
	private String label;

	private DataStatus(Integer value, String label) {
		this.value = value;
		this.label = label;
	}

	public Integer value() {
		return value;
	}

	public String label() {
		return label;
	}

}
