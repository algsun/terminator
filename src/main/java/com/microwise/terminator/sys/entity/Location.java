package com.microwise.terminator.sys.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "m_location")
@Data
public class Location {
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 位置点名称
     */
    @NotEmpty
    @Column(name = "locationName")
    private String locationname;
    /**
     * 节点id
     */
    @Column(name = "nodeId")
    private String nodeid;
    /**
     * 文物id
     */
    @Column(name = "zoneId")
    private String zoneid;
    /**
     * 文物名称
     */
    @Transient
    private String zoneName;
    /**
     * 站点编号
     */
    @Column(name = "siteId")
    private String siteid;
    /**
     * 开始时间
     */
    @Column(name = "createTime")
    private Date createtime;
    /**
     * 位置点类型：0:设备位置点;1:批次位置点;
     */
    private Integer type;

    private String remark;
    /**
     * 文物图片
     */
    private String photo;
    /**
     * 文物
     */
    @Transient
    private Relic relic;
    @Transient
    private List<Sensorinfo> sensorinfoList;
    @Transient
    private Device device;
}