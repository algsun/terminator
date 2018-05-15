package com.microwise.terminator.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 位置点数据VO对象（封装实时数据/历史数据监测指标对应值）
 *
 * @author liuzhu
 * @date 2014-7-1
 */
public class LocationDataVO {

    // 高于阈值
    public static final int SEND_STATE_MORE_THEN_MAX = 1;
    // 低于阈值
    public static final int SEND_STATE_LESS_THEN_MIN = 2;
    // 正常
    public static final int SEND_STATE_NORMAL = 3;

    /**
     * 设备ID
     */
    private String locationId;

    private String locationName;

    /**
     * 监测指标标识
     */
    private int sensorPhysicalid;

    /**
     * 监测指标中文名称
     */
    private String cnName;

    /**
     * 监测指标英文名称
     */
    private String enName;

    /**
     * 监测指标单位
     */
    private String units;

    /**
     * 监测指标值
     */
    private String sensorPhysicalValue;

    /**
     * 监测指标值状态 0：采样失败 0xFFFF为采样失败 1：采样正常 2：低于低阈值 3：超过高阈值 4：空数据（前台暂不处理）
     */
    private int state;

    /**
     * 当前监测指标的采样时间
     */
    private Date stamp;

    /**
     * 监测指标类型 0: 普通类型 1：风向类 类型
     */
    private int showType;

    /**
     * 区域信息id
     */
    private String zoneId;

    /**
     * 节点类型
     */
    private int nodeType;

    // conditionType, target, floating 值用于判断数据状态（超标 or 正常，为 getState 提供数据依据）
    /**
     * 条件类型
     * 1-范围；2-大于；3-小于；4-大于等于；5-小于等于
     */
    private int conditionType;

    /**
     * 目标值
     */
    private float target;
    /**
     * 浮动值
     */
    private float floating;

    /**
     * 是否节点？
     */
    private boolean isNode;

    /**
     * 监测指标的最大值
     */
    private String bigValue;

    /**
     * 监测指标的最小值
     */
    private String smallValue;

    /**
     * 均值
     */
    private String avgValue;

    /**
     * 监测值 map集合
     */
    private Map<Long, LocationDataVO> sensorPhysicalValueMap;

    /**
     * `
     * locationData集合
     */
    private List<LocationDataVO> locationDataVOList;

    /**
     * 获取监测值状态
     * 实时数据状态颜色需要调用，虽然方法名显示为灰色，勿删
     *
     * @return
     */
    public int getValueState() {
        int state = SEND_STATE_NORMAL;
        if (showType == 1) {
            return state;
        }
        double sensorValue = Double.parseDouble(sensorPhysicalValue);
        switch (conditionType) {
            case 1:
                float max = target + floating;
                float min = target - floating;
                if (!(min <= sensorValue && max >= sensorValue)) {
                    if (sensorValue > max) {
                        state = SEND_STATE_MORE_THEN_MAX;
                    } else if (sensorValue < min) {
                        state = SEND_STATE_LESS_THEN_MIN;
                    }
                }
                break;
            case 2:
                if (sensorValue <= target) state = SEND_STATE_LESS_THEN_MIN;
                break;
            case 3:
                if (sensorValue >= target) state = SEND_STATE_MORE_THEN_MAX;
                break;
            case 4:
                if (sensorValue < target) state = SEND_STATE_LESS_THEN_MIN;
                break;
            case 5:
                if (sensorValue > target) state = SEND_STATE_MORE_THEN_MAX;
                break;
            default:
                state = SEND_STATE_NORMAL;
                break;
        }

        return state;
    }


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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    public Map<Long, LocationDataVO> getSensorPhysicalValueMap() {
        return sensorPhysicalValueMap;
    }

    public void setSensorPhysicalValueMap(Map<Long, LocationDataVO> sensorPhysicalValueMap) {
        this.sensorPhysicalValueMap = sensorPhysicalValueMap;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setConditionType(int conditionType) {
        this.conditionType = conditionType;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public void setFloating(float floating) {
        this.floating = floating;
    }

    public boolean isNode() {
        return isNode;
    }

    public String getBigValue() {
        return bigValue;
    }

    public void setBigValue(String bigValue) {
        this.bigValue = bigValue;
    }

    public String getSmallValue() {
        return smallValue;
    }

    public void setSmallValue(String smallValue) {
        this.smallValue = smallValue;
    }

    public void setNode(boolean isNode) {
        this.isNode = isNode;
    }

    public List<LocationDataVO> getLocationDataVOList() {
        return locationDataVOList;
    }

    public void setLocationDataVOList(List<LocationDataVO> locationDataVOList) {
        this.locationDataVOList = locationDataVOList;
    }

    public String getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(String avgValue) {
        this.avgValue = avgValue;
    }

    @Override
    public String toString() {
        return "DeviceDataVO [sensorPhysicalid=" + sensorPhysicalid
                + ", sensorPhysicalValue=" + sensorPhysicalValue + ", state="
                + state + ", stamp=" + stamp + ", showType=" + showType + "]";
    }

}
