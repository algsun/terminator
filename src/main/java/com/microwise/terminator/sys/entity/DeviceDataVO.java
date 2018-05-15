package com.microwise.terminator.sys.entity;

import java.util.Date;

/**
 * 设备数据VO对象（封装实时数据/历史数据监测指标对应值）
 * TODO 位置点改完之后删除 liuzhu 2014-7-1
 * 
 * @author zhangpeng
 * @date 2013-1-23
 */
public class DeviceDataVO {

	/** 监测指标标识 */
	private int sensorPhysicalid;
	
	/** 监测指标中文名称*/
	private String cnName;
	
	/** 监测指标单位*/
	private String units;

	/** 监测指标值 */
	private String sensorPhysicalValue;

	/** 监测指标值状态 0：采样失败 0xFFFF为采样失败 1：采样正常 2：低于低阈值 3：超过高阈值 4：空数据（前台暂不处理） */
	private int state;

	/** 当前监测指标的采样时间 */
	private Date stamp;

	/** 监测指标类型 0: 普通类型 1：风向类 类型 */
	private int showType;

	public int getSensorPhysicalid() {
		return sensorPhysicalid;
	}

	public void setSensorPhysicalid(int sensorPhysicalid) {
		this.sensorPhysicalid = sensorPhysicalid;
	}

	public String getSensorPhysicalValue() {
		return sensorPhysicalValue;
	}

	public void setSensorPhysicalValue(String sensorPhysicalValue) {
		this.sensorPhysicalValue = sensorPhysicalValue;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getStamp() {
		return stamp;
	}

	public void setStamp(Date stamp) {
		this.stamp = stamp;
	}

	public int getShowType() {
		return showType;
	}

	public void setShowType(int showType) {
		this.showType = showType;
	}
	
	

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "DeviceDataVO [sensorPhysicalid=" + sensorPhysicalid
				+ ", sensorPhysicalValue=" + sensorPhysicalValue + ", state="
				+ state + ", stamp=" + stamp + ", showType=" + showType + "]";
	}

}
