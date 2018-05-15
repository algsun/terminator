package com.microwise.terminator.sys.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "sys_menu")
@Data
public class Menu implements Serializable{

    public Menu() {
    }

    public Menu(String id) {
        this.id = id;
    }

    /**
     * 编号
     */
    @Id
    private String id;

    /**
     * 父级编号
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 所有父级编号
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 链接
     */
    private String href;

    /**
     * 目标
     */
    private String target;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否在菜单中显示
     */
    @Column(name = "is_show")
    private Boolean isShow;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    private Boolean delFlag;


    @Transient
    private Menu parent;


    @JsonIgnore
    public static String getRootId() {
        return "1";
    }


    @JsonBackReference
    @NotNull
    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    @JsonIgnore
    public static void sortList(List<Menu> list, List<Menu> sourceList, String parentId, boolean cascade) {

        for (Menu menu : sourceList) {
            if (menu.getParent() != null && menu.getParent().getId() != null
                    && menu.getParent().getId().equals(parentId)) {
                list.add(menu);
                if (cascade) {
                    // 判断是否还有子节点, 有则继续获取子节点
                    for (Menu child : sourceList) {
                        if (child.getParent() != null && child.getParent().getId() != null
                                && child.getParent().getId().equals(menu.getId())) {
                            sortList(list, sourceList, menu.getId(), true);
                            break;
                        }
                    }
                }
            }
        }
    }
}