package com.microwise.terminator.sys.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sys_office")
@Data
public class Office implements Serializable {
    /**
     * 机构编号
     */
    @Id
    private Integer id;

    /**
     * 机构名称
     */
    @NotEmpty(message = "机构名称不能为空")
    @Column(name = "office_name")
    private String officeName;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    private Boolean delFlag;

    public Office() {
        this.delFlag = false;
    }

    public void preInsert(Office office) {
        office.setCreateDate(new Date());
    }
}