package com.microwise.terminator.sys.entity;

import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.util.UserUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "sys_relic")
@Data
public class Relic {
    /**
     * 文物id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 文物名称
     */
    private String name;

    /**
     * 时代id
     */
    @Column(name = "era_id")
    private String eraId;

    /**
     * 质地id
     */
    @Column(name = "texture_id")
    private String textureId;

    /**
     * 级别id
     */
    @Column(name = "level_id")
    private String levelId;

    /**
     * 机构id
     */
    @Column(name = "office_id")
    private String officeId;

    /**
     * 经度
     */
    @Column(name = "lng")
    private Double lng;

    /**
     * 纬度
     */
    @Column(name = "lat")
    private Double lat;

    /**
     * 照片id
     */
    @Column(name = "photo_id")
    private String photoId;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 删除标志：0.未删除 1.删除
     */
    @Column(name = "del_flag")
    private Boolean delFlag;

    /**
     * 文物级别
     */
    @Transient
    private Level level;
    /**
     * 文物时代
     */
    @Transient
    private Era era;
    /**
     * 文物质地
     */
    @Transient
    private Texture texture;
    /**
     * 文物照片
     */
    @Transient
    private Photo photo;
    /**
     * 文物下挂载的所有设备
     */
    @Transient
    private List<Device> deviceList;
    /**
     * 文物的报警记录
     */
    @Transient
    private List<AlarmRecord> alarmRecordList;
    /**
     * 文物下设备及报警的统计
     */
    @Transient
    private Map<String, Object> count;

    public void preInsert() {
        //setId(IdGen.uuid());
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user.getId();
            this.createBy = user.getId();
            this.officeId = user.getOfficeId();
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    public void preUpdate() {
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user.getId();
            this.officeId = user.getOfficeId();
        }
        this.updateDate = new Date();
    }
}