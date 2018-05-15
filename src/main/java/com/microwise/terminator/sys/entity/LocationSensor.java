package com.microwise.terminator.sys.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_location_sensor")
public class LocationSensor {
    /**
     * uuid
     */
    @Id
    private String id;

    /**
     * 位置点唯一标识
     */
    @Column(name = "locationId")
    private String locationid;

    /**
     * 传感量标识
     */
    @Column(name = "sensorPhysicalId")
    private Integer sensorphysicalid;

    /**
     * 传感量值
     */
    @Column(name = "sensorPhysicalValue")
    private String sensorphysicalvalue;

    /**
     * 0：采样失败  0xFFFF为采样失败
            1：采样正常
            2：低于低阈值
            3：超过高阈值
            4：空数据（前台暂不处理）
     */
    private Integer state;

    /**
     * 数据采样时间戳（实时数据显示时采用一组数据中时间最大值）
     */
    private Date stamp;

    /**
     * 数据同步版本
     */
    @Column(name = "dataVersion")
    private Long dataversion;

    /**
     * 获取uuid
     *
     * @return id - uuid
     */
    public String getId() {
        return id;
    }

    /**
     * 设置uuid
     *
     * @param id uuid
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取位置点唯一标识
     *
     * @return locationId - 位置点唯一标识
     */
    public String getLocationid() {
        return locationid;
    }

    /**
     * 设置位置点唯一标识
     *
     * @param locationid 位置点唯一标识
     */
    public void setLocationid(String locationid) {
        this.locationid = locationid;
    }

    /**
     * 获取传感量标识
     *
     * @return sensorPhysicalId - 传感量标识
     */
    public Integer getSensorphysicalid() {
        return sensorphysicalid;
    }

    /**
     * 设置传感量标识
     *
     * @param sensorphysicalid 传感量标识
     */
    public void setSensorphysicalid(Integer sensorphysicalid) {
        this.sensorphysicalid = sensorphysicalid;
    }

    /**
     * 获取传感量值
     *
     * @return sensorPhysicalValue - 传感量值
     */
    public String getSensorphysicalvalue() {
        return sensorphysicalvalue;
    }

    /**
     * 设置传感量值
     *
     * @param sensorphysicalvalue 传感量值
     */
    public void setSensorphysicalvalue(String sensorphysicalvalue) {
        this.sensorphysicalvalue = sensorphysicalvalue;
    }

    /**
     * 获取0：采样失败  0xFFFF为采样失败
            1：采样正常
            2：低于低阈值
            3：超过高阈值
            4：空数据（前台暂不处理）
     *
     * @return state - 0：采样失败  0xFFFF为采样失败
            1：采样正常
            2：低于低阈值
            3：超过高阈值
            4：空数据（前台暂不处理）
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置0：采样失败  0xFFFF为采样失败
            1：采样正常
            2：低于低阈值
            3：超过高阈值
            4：空数据（前台暂不处理）
     *
     * @param state 0：采样失败  0xFFFF为采样失败
            1：采样正常
            2：低于低阈值
            3：超过高阈值
            4：空数据（前台暂不处理）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取数据采样时间戳（实时数据显示时采用一组数据中时间最大值）
     *
     * @return stamp - 数据采样时间戳（实时数据显示时采用一组数据中时间最大值）
     */
    public Date getStamp() {
        return stamp;
    }

    /**
     * 设置数据采样时间戳（实时数据显示时采用一组数据中时间最大值）
     *
     * @param stamp 数据采样时间戳（实时数据显示时采用一组数据中时间最大值）
     */
    public void setStamp(Date stamp) {
        this.stamp = stamp;
    }

    /**
     * 获取数据同步版本
     *
     * @return dataVersion - 数据同步版本
     */
    public Long getDataversion() {
        return dataversion;
    }

    /**
     * 设置数据同步版本
     *
     * @param dataversion 数据同步版本
     */
    public void setDataversion(Long dataversion) {
        this.dataversion = dataversion;
    }
}