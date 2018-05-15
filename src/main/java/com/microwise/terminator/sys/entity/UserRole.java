package com.microwise.terminator.sys.entity;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "sys_user_role")
public class UserRole {
    /**
     * 用户编号
     */
    @Id
    @Column(name = "user_id")
    private String userId;

    /**
     * 角色编号
     */
    @Id
    @Column(name = "role_id")
    private String roleId;

    public UserRole() {
    }

    public UserRole(String userId) {
        this.userId = userId;
    }

    public UserRole(String userId, String roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }


}