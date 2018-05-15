package com.microwise.terminator.sys.entity;

import javax.persistence.*;

@Table(name = "sys_era")
public class Era {
    public Era() {
    }

    public Era(String name) {
        this.name = name;
    }

    /**
     * 时代id
     */
    @Id
    private String id;

    /**
     * 时代名称
     */
    private String name;

    /**
     * 获取时代id
     *
     * @return id - 时代id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置时代id
     *
     * @param id 时代id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取时代名称
     *
     * @return name - 时代名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置时代名称
     *
     * @param name 时代名称
     */
    public void setName(String name) {
        this.name = name;
    }
}