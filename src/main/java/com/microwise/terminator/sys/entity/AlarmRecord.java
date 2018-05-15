package com.microwise.terminator.sys.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AlarmRecord implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 系统类型
     */
    private String systemtype;

    /**
     * 系统来源id
     */
    private String sourceid;

    /**
     * 报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警
     */
    private Boolean alarmpointtype;

    /**
     * 报警点id
     */
    private String alarmpointid;


    /**
     * 监测指标id
     */
    private Integer sensorid;

    /**
     * 因素
     */
    private String factor;

    /**
     * 报警时间
     */
    private Date alarmtime;

    /**
     * 字符串类型的报警事件
     */
    private String alarmTimeStr;

    /**
     * 处理状态：0：待处理 1：已处理 -1：全部
     */
    private Integer state;

    /**
     * 处理人
     */
    private String transactor;

    /**
     * 处理时间
     */
    private Date handletime;

    /**
     * 处理措施
     */
    private String handlemeasure;

    /**
     * 处理结果：0. 未恢复正常 1. 已恢复正常
     */
    private Integer handleresult;
    /**
     * 通知人json对象
     */
    private String notifier;

    /**
     * 机构
     */
    private Office office;

    /**
     * 文物
     */
    private Relic relic;

    /**
     * 处理人对象
     */
    private User user;

    /**
     * 通知人
     */
    private List<Notifier> notifiers;

    /**
     * 通知人json对象 => 通知人实体对象
     *
     * @return
     */
    public void setNotifiers() {
        Gson gson = new Gson();
        this.notifiers = gson.fromJson(getNotifier(), new TypeToken<List<Notifier>>() {
        }.getType());
    }
}