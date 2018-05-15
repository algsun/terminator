package com.microwise.terminator.sys.entity;

import javax.persistence.*;

@Table(name = "m_sensorinfo")
public class Sensorinfo {
    @Id
    private Integer id;

    /**
     * 传感量标识
     */
    @Column(name = "sensorPhysicalid")
    private Integer sensorphysicalid;

    /**
     * 转义传感量标识
     */
    @Column(name = "escape_sensor_id")
    private Integer escapeSensorId;

    /**
     * 传感量缩写
     */
    @Column(name = "en_name")
    private String enName;

    /**
     * 监测量中文名
     */
    @Column(name = "cn_name")
    private String cnName;

    /**
     * 传感量精度
     */
    @Column(name = "sensorPrecision")
    private Integer sensorprecision;

    /**
     * 计量单位
     */
    private String units;

    /**
     * 显示位
     */
    private Integer positions;

    /**
     * 是否有效 1：有效    0：无效
     */
    @Column(name = "isActive")
    private Integer isactive;

    /**
     * 0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;
     */
    @Column(name = "showType")
    private Integer showtype;

    /**
     * 允许的最小值
     */
    @Column(name = "minValue")
    private Double minvalue;

    /**
     * 允许的最大值
     */
    @Column(name = "`maxValue`")
    private Double maxvalue;

    /**
     * 无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     */
    @Column(name = "rangeType")
    private Integer rangetype;

    /**
     * 原始值是否有符号。无符号 0; 有符号 1;
     */
    @Column(name = "signType")
    private Integer signtype;

    /**
     * 达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关
     */
    @Column(name = "conditionType")
    private Integer conditiontype;

    /**
     * 文保行业监测调控预期目标值
     */
    private Float target;

    /**
     * 浮动值：以目标值为中心的浮动范围
     */
    private Float floating;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取传感量标识
     *
     * @return sensorPhysicalid - 传感量标识
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
     * 获取转义传感量标识
     *
     * @return escape_sensor_id - 转义传感量标识
     */
    public Integer getEscapeSensorId() {
        return escapeSensorId;
    }

    /**
     * 设置转义传感量标识
     *
     * @param escapeSensorId 转义传感量标识
     */
    public void setEscapeSensorId(Integer escapeSensorId) {
        this.escapeSensorId = escapeSensorId;
    }

    /**
     * 获取传感量缩写
     *
     * @return en_name - 传感量缩写
     */
    public String getEnName() {
        return enName;
    }

    /**
     * 设置传感量缩写
     *
     * @param enName 传感量缩写
     */
    public void setEnName(String enName) {
        this.enName = enName;
    }

    /**
     * 获取监测量中文名
     *
     * @return cn_name - 监测量中文名
     */
    public String getCnName() {
        return cnName;
    }

    /**
     * 设置监测量中文名
     *
     * @param cnName 监测量中文名
     */
    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    /**
     * 获取传感量精度
     *
     * @return sensorPrecision - 传感量精度
     */
    public Integer getSensorprecision() {
        return sensorprecision;
    }

    /**
     * 设置传感量精度
     *
     * @param sensorprecision 传感量精度
     */
    public void setSensorprecision(Integer sensorprecision) {
        this.sensorprecision = sensorprecision;
    }

    /**
     * 获取计量单位
     *
     * @return units - 计量单位
     */
    public String getUnits() {
        return units;
    }

    /**
     * 设置计量单位
     *
     * @param units 计量单位
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * 获取显示位
     *
     * @return positions - 显示位
     */
    public Integer getPositions() {
        return positions;
    }

    /**
     * 设置显示位
     *
     * @param positions 显示位
     */
    public void setPositions(Integer positions) {
        this.positions = positions;
    }

    /**
     * 获取是否有效 1：有效    0：无效
     *
     * @return isActive - 是否有效 1：有效    0：无效
     */
    public Integer getIsactive() {
        return isactive;
    }

    /**
     * 设置是否有效 1：有效    0：无效
     *
     * @param isactive 是否有效 1：有效    0：无效
     */
    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    /**
     * 获取0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;
     *
     * @return showType - 0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;
     */
    public Integer getShowtype() {
        return showtype;
    }

    /**
     * 设置0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;
     *
     * @param showtype 0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类;
     */
    public void setShowtype(Integer showtype) {
        this.showtype = showtype;
    }

    /**
     * 获取允许的最小值
     *
     * @return minValue - 允许的最小值
     */
    public Double getMinvalue() {
        return minvalue;
    }

    /**
     * 设置允许的最小值
     *
     * @param minvalue 允许的最小值
     */
    public void setMinvalue(Double minvalue) {
        this.minvalue = minvalue;
    }

    /**
     * 获取允许的最大值
     *
     * @return maxValue - 允许的最大值
     */
    public Double getMaxvalue() {
        return maxvalue;
    }

    /**
     * 设置允许的最大值
     *
     * @param maxvalue 允许的最大值
     */
    public void setMaxvalue(Double maxvalue) {
        this.maxvalue = maxvalue;
    }

    /**
     * 获取无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     *
     * @return rangeType - 无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     */
    public Integer getRangetype() {
        return rangetype;
    }

    /**
     * 设置无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     *
     * @param rangetype 无范围限制 0; 只有最小值限制 1; 只有最大值限制 2; 两个都有 3;
     */
    public void setRangetype(Integer rangetype) {
        this.rangetype = rangetype;
    }

    /**
     * 获取原始值是否有符号。无符号 0; 有符号 1;
     *
     * @return signType - 原始值是否有符号。无符号 0; 有符号 1;
     */
    public Integer getSigntype() {
        return signtype;
    }

    /**
     * 设置原始值是否有符号。无符号 0; 有符号 1;
     *
     * @param signtype 原始值是否有符号。无符号 0; 有符号 1;
     */
    public void setSigntype(Integer signtype) {
        this.signtype = signtype;
    }

    /**
     * 获取达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关
     *
     * @return conditionType - 达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关
     */
    public Integer getConditiontype() {
        return conditiontype;
    }

    /**
     * 设置达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关
     *
     * @param conditiontype 达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于。与目标值/浮动值有关
     */
    public void setConditiontype(Integer conditiontype) {
        this.conditiontype = conditiontype;
    }

    /**
     * 获取文保行业监测调控预期目标值
     *
     * @return target - 文保行业监测调控预期目标值
     */
    public Float getTarget() {
        return target;
    }

    /**
     * 设置文保行业监测调控预期目标值
     *
     * @param target 文保行业监测调控预期目标值
     */
    public void setTarget(Float target) {
        this.target = target;
    }

    /**
     * 获取浮动值：以目标值为中心的浮动范围
     *
     * @return floating - 浮动值：以目标值为中心的浮动范围
     */
    public Float getFloating() {
        return floating;
    }

    /**
     * 设置浮动值：以目标值为中心的浮动范围
     *
     * @param floating 浮动值：以目标值为中心的浮动范围
     */
    public void setFloating(Float floating) {
        this.floating = floating;
    }
}