package com.microwise.terminator.sys.entity;

import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 设备对象
 *
 * @author sun.cong
 * @date 2013-1-17
 */
@Data
@Table(name = "m_nodeinfomemory")
public class Device extends NodeInfo {
    /**
     * 设备接收信号强度
     */
    private Integer rssi;
    /**
     * 设备链路质量
     */
    private Integer lqi;
    /**
     * 设备状态：-1、超时, 0、正常, 1、低电压, 2、掉电
     */
    private Integer anomaly;
    /**
     * 设备工作周期，单位：秒
     */
    private Integer interval_i;
    /**
     * 振动灵敏度（1：高；2：中；3：低）
     */
    private Integer sensitivity;
    /**
     * 时间戳
     */
    private Date stamp;
    /**
     * 是否可控：0：可控 1：不可控
     */
    private Integer isControl;
    /**
     * 当前节点IP号
     */
    private Integer childIP;
    /**
     * 父节点IP号
     */
    private Integer parentIP;
    /**
     * 设备预热时间
     */
    private Integer warmUp;
    /**
     * 此设备是否具有振动监测
     */
    @Transient
    private Boolean hasShake;
    /**
     * 设备所在位置点
     */
    @Transient
    private Location location;
    /**
     * 设备所在文物
     */
    @Transient
    private Relic relic;
    /**
     * 工作周期是否改变
     */
    @Transient
    private Boolean isIntervalChange;
    /**
     * 灵敏度是否改变
     */
    @Transient
    private Boolean isSensitivityChange;
}