package com.microwise.terminator.sys.entity;

import javax.persistence.*;

@Table(name = "sys_texture")
public class Texture {

    public Texture() {
    }

    public Texture(String name, String enname) {
        this.name = name;
        this.enname = enname;
    }

    /**
     * 质地id
     */
    @Id
    private String id;

    /**
     * 质地名称
     */
    private String name;

    /**
     * 质地英文名称
     */
    @Column(name = "enName")
    private String enname;

    /**
     * 获取质地id
     *
     * @return id - 质地id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置质地id
     *
     * @param id 质地id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取质地名称
     *
     * @return name - 质地名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置质地名称
     *
     * @param name 质地名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取质地英文名称
     *
     * @return enName - 质地英文名称
     */
    public String getEnname() {
        return enname;
    }

    /**
     * 设置质地英文名称
     *
     * @param enname 质地英文名称
     */
    public void setEnname(String enname) {
        this.enname = enname;
    }
}