package com.microwise.terminator.sys.entity;

import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.util.UserUtils;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_photo")
public class Photo {
    public Photo() {
    }

    /**
     * 照片id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 底片号
     */
    @Column(name = "filmCode")
    private String filmcode;

    /**
     * 摄影师
     */
    private String photographer;

    /**
     * 图片比例
     */
    private String ratio;

    /**
     * 图片路径
     */
    private String path;

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
     * 获取照片id
     *
     * @return id - 照片id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置照片id
     *
     * @param id 照片id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取底片号
     *
     * @return filmCode - 底片号
     */
    public String getFilmcode() {
        return filmcode;
    }

    /**
     * 设置底片号
     *
     * @param filmcode 底片号
     */
    public void setFilmcode(String filmcode) {
        this.filmcode = filmcode;
    }

    /**
     * 获取摄影师
     *
     * @return photographer - 摄影师
     */
    public String getPhotographer() {
        return photographer;
    }

    /**
     * 设置摄影师
     *
     * @param photographer 摄影师
     */
    public void setPhotographer(String photographer) {
        this.photographer = photographer;
    }

    /**
     * 获取图片比例
     *
     * @return ratio - 图片比例
     */
    public String getRatio() {
        return ratio;
    }

    /**
     * 设置图片比例
     *
     * @param ratio 图片比例
     */
    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    /**
     * 获取图片路径
     *
     * @return path - 图片路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置图片路径
     *
     * @param path 图片路径
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_date - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void preInsert() {
        //setId(IdGen.uuid());
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())) {
            this.updateBy = user.getId();
            this.createBy = user.getId();
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }

    public void preUpdate(){
        User user = UserUtils.getCurrentUser();
        if (StringUtils.isNotBlank(user.getId())){
            this.updateBy = user.getId();
        }
        this.updateDate = new Date();
    }
}