package com.yinker.etl.trans.model.base;

import java.util.List;

public class TimeAnalyzeJson {

	private List<String[]> dataList;

	private String startValue;

	public List<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}

	public String getStartValue() {
		return startValue;
	}

	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}

}
