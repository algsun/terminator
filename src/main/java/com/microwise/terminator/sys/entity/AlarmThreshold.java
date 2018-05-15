package com.microwise.terminator.sys.entity;

import lombok.Data;

@Data
public class AlarmThreshold {
    /**
     * id
     */
    private Integer id;

    /**
     * 报警策略id
     */
    private String alarmstrategyid;

    /**
     * 监测指标id
     */
    private Integer sensorphysicalid;

    /**
     * 达标条件类型，1-范围；2-大于；3-小于；4-大于等于；5-小于等于;与目标值/浮动值有关
     */
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
     * 0 默认; 1 风向类；该字段用于呈现判断，风向类在实时数据、历史数据中需要展示为方向标识，而在图表中需要具体数值，考虑扩展性，加入此标识; 2 GPS 类; 3.开关
     */
    private Integer showtype;
    /**
     *监测指标中文名称
     */
    private  String cnName;
    /**
     * 监测指标单位
     */
    private  String units;


}