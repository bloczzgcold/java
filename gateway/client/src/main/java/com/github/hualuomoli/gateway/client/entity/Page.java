package com.github.hualuomoli.gateway.client.entity;

import java.util.List;

public class Page<T> {

	/** 当前页码 */
	private Integer pageNo;
	/** 每页数量 */
	private Integer pageSize;
	/** 总数据量 */
	private Integer count;
	/** 数据集合 */
	private List<T> dataList;

	public Page() {
	}

	public Page(Integer pageNo, Integer pageSize, Integer count, List<T> dataList) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.count = count;
		this.dataList = dataList;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

}
