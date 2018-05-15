package com.microwise.terminator.sys.entity;

import com.microwise.terminator.common.persistence.BaseEntity;
import com.microwise.terminator.sys.util.UserUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "sys_user")
public class User extends BaseEntity<User> {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 所属机构
     */
    @Column(name = "office_id")
    private String officeId;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 最后登陆IP
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Column(name = "login_date")
    private Date loginDate;

    /**
     * 是否可登录
     */
    @Column(name = "login_flag")
    private Boolean loginFlag;

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
     * 修改者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 删除标记
     */
    @Column(name = "del_flag")
    private Boolean delFlag;

    /**
     * 是否关注
     */
    private Integer attention;

    @Transient
    private String oldLoginName;// 原登录名
    @Transient
    private String newPassword;    // 新密码

    @Transient
    private String oldLoginIp;    // 上次登陆IP
    @Transient
    private Date oldLoginDate;    // 上次登陆日期


    public User() {
        this.delFlag = false;
    }

    public User(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 用户拥有的所有角色
     */
    @Transient
    private List<Role> roles;

    /**
     * 用户所属机构
     */
    @Transient
    private Office office;
    /**
     * 用户角色关联数据
     */
    @Transient
    private String[] rolesId;

    /**
     * 是否管理员
     *
     * @return
     */
    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    /**
     * 是否管理员
     *
     * @return
     */
    public static boolean isAdmin(String id) {
        return id != null && "1".equals(id);
    }

    @Override
    public void preInsert() {
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.createBy = user.getId();
            this.updateBy = user.getId();
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    @Override
    public void preUpdate() {
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user.getId();
        }
        this.updateDate = new Date();
    }
}