package com.microwise.terminator.sys.entity;

import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class AlarmStrategy {

    /**
     * 主键id
     */
    private String id;

    /**
     * 系统id
     */
    private String systemid;

    /**
     * 系统来源id
     */
    private String sourceid;

    /**
     * 报警点id
     */
    private String alarmpointid;

    /**
     * 报警通知人
     */
    private String notifier;

    private List<Notifier> notifiers = Lists.newArrayList();

    public void setNotifierIds(List<String> notifierIds) {
        for (String notifierId : notifierIds) {
            Notifier notifier = new Notifier();
            notifier.setId(notifierId);
            notifiers.add(notifier);
        }
    }


    /**
     * 策略名称
     */
    private String name;

    /**
     * 报警方式：1.邮件 2.短信 3.语音短信
     */
    private Integer alarmapproach;

    /**
     * 报警点类型： 1.基于区域报警 2.基于文物报警 3.基于位置点报警
     */
    private Integer alarmpointtype;

    /**
     * 免打扰时间段：开始时间
     */
    @DateTimeFormat(pattern = "HH:mm")
    private Date alarmbegintime;

    /**
     * 免打扰时间段：结束时间
     */
    @DateTimeFormat(pattern = "HH:mm")
    private Date alarmendtime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createtime = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatetime = new Date();

    private List<AlarmThreshold> alarmthresholds;

}