package com.microwise.terminator.sys.entity;

import lombok.Data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "m_nodeinfo")
@Data
public class NodeInfo {
    /**
     * 产品入网唯一标识
     */
    @Id
    private String nodeid;
    /**
     * 1：节点  2：中继  3:节点-主模块(可控) 4:节点-从模块(可控) 5:控制模块 7：网关
     */
    @Column(name = "nodeType")
    private Integer nodetype;
    /**
     * 节点创建时间或更新时间，与原add_time字段合并
     * （记录生成后不可修改）
     */
    @Column(name = "createTime")
    private Date createtime;
    /**
     * X轴坐标
     */
    @Column(name = "X")
    private Integer x;
    /**
     * Y轴坐标
     */
    @Column(name = "Y")
    private Integer y;
    /**
     * Z轴坐标
     */
    @Column(name = "Z")
    private Integer z;
    /**
     * 站点id
     */
    @Column(name = "siteId")
    private String siteid;
    /**
     * 系统相对路径和名称
     */
    @Column(name = "deviceImage")
    private String deviceimage;
    /**
     * 数据版本
     */
    @Column(name = "dataVersion")
    private Long dataversion;
    /**
     * 绑定状态：0 未绑定  1已绑定
     */
    private Integer binding;
    /**
     * 设备状态：0 无效  1有效
     */
    @Column(name = "isActive")
    private Integer isactive;
    /**
     * 产品序列号
     */
    private String sn;
    /**
     * 上传状态：0 未上传 1 已上传
     */
    @Column(name = "uploadState")
    private Integer uploadstate;
    /**
     * 是否有温度补偿：0 否，1 是
     */
    @Column(name = "isHumCompensate")
    private Integer ishumcompensate;
    /**
     * 是否是恒湿机：0 否，1 是
     */
    @Column(name = "isHumdityDevice")
    private Integer ishumditydevice;
    /**
     * 设备电压阈值
     */
    @Column(name = "voltageThreshold")
    private Float voltagethreshold;
    /**
     * 标记查询普通信息还是详细信息
     */
    @Transient
    private Boolean flag;
    /**
     * 是否分页(默认分页)
     */
    @Transient
    private Boolean paging;
    /**
     * 正常设备与异常设备的标记
     */
    @Transient
    private Integer normalFlag;
    /**
     * 站点
     */
    @Transient
    private Office office;

    public NodeInfo() {
        this.flag = true;
    }

    public NodeInfo(String nodeid) {
        this.nodeid = nodeid;
        this.flag = true;
    }
}