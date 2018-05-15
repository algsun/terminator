package com.microwise.terminator.sys.entity;

import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import javax.persistence.*;

@Table(name = "m_location_history")
@Data
public class LocationHistory {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 位置点id
     */
    @Column(name = "locationId")
    private String locationid;

    /**
     * 节点id
     */
    @Column(name = "nodeId")
    private String nodeid;

    /**
     * 开始时间
     */
    @Column(name = "startTime")
    private Date starttime;

    /**
     * 结束时间
     */
    @Column(name = "endTime")
    private Date endtime;

    public LocationHistory() {
    }
}