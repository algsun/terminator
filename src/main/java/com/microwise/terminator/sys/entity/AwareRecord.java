package com.microwise.terminator.sys.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AwareRecord implements Serializable {
    /**
     * 主键
     */
    private String id;

    /**
     * 报警记录id
     */
    private String alarmRecordId;

    /**
     * 人员
     */
    private String awareNotifier;

    /**
     * 知晓人
     */
    private User awareUser;

    /**
     * 时间
     */
    private Date awareTime;

    /**
     * 报警记录
     */
    private AlarmRecord alarmRecord;
}