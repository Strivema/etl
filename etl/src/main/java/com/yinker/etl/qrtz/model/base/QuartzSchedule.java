package com.yinker.etl.qrtz.model.base;

import org.zwork.framework.base.support.BasePageQuery;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class QuartzSchedule extends BasePageQuery implements Serializable {

	private static final long serialVersionUID = -536480398093621017L;

	private String id;
	private String name;
	private String group;
	private String status;
	private String nextTime;
	private String prevTime;
	private String repeat;
	private String interval;
	private String executor;
	private String execType;
	private String execTime;
	private String startDate;
	private String endDate;

	private String cron;

	private String cycle;
	private String remote;
	private String version;
	private String description;

	private Map<String, Object> map = new HashMap<String, Object>();

	public void setValue(String key, String strVal) {
		if ("id".equals(key)) {
			this.setId(strVal);
		} else if ("group".equals(key)) {
			this.setGroup(strVal);
		} else if ("title".equals(key)) {
			this.setName(strVal);
		} else if ("executor".equals(key)) {
			this.setExecutor(strVal);
		} else if ("cron".equals(key)) {
			this.setCron(strVal);
		} else if ("description".equals(key)) {
			this.setDescription(strVal);
		} else {
			map.put(key, strVal);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNextTime() {
		return nextTime;
	}

	public void setNextTime(String nextTime) {
		this.nextTime = nextTime;
	}

	public String getPrevTime() {
		return prevTime;
	}

	public void setPrevTime(String prevTime) {
		this.prevTime = prevTime;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getExecType() {
		return execType;
	}

	public void setExecType(String execType) {
		this.execType = execType;
	}

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public String getRemote() {
		return remote;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}