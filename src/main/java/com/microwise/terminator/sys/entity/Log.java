package com.microwise.terminator.sys.entity;

import com.microwise.terminator.common.persistence.BaseEntity;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.util.UserUtils;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Map;
import javax.persistence.*;

@Table(name = "sys_log")
@Data
public class Log extends BaseEntity<Log> {
    /**
     * 开始时间
     */
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;
    /**
     * 结束时间
     */
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    /**
     * 机构名称
     */
    @Transient
    private String officeName;
    /**
     * 创建者用户名
     */
    @Transient
    private String createByName;
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 日志类型(1:接入日志，2：错误日志)
     */
    private Short type;

    /**
     * 日志标题（操作菜单）
     */
    private String title;

    /**
     * 创建者（创建者用户id）
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 操作IP地址
     */
    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 用户代理
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 请求URI
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 异常信息
     */
    private String exception;


    // 日志类型（1：接入日志；2：错误日志）
    public static final short TYPE_ACCESS = 1;
    public static final short TYPE_EXCEPTION = 2;

    public Log() {
        this.type = TYPE_ACCESS;
    }

    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setParams(Map paramMap) {
        if (paramMap == null) {
            return;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
        }
        this.params = params.toString();
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public void preInsert() {
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.createBy = user.getId();
        }
        this.createDate = new Date();
    }

    @Override
    public void preUpdate() {
    }
}