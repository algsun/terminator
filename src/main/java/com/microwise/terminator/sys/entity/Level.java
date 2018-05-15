package com.microwise.terminator.sys.entity;

import javax.persistence.*;

@Table(name = "sys_level")
public class Level {

    public Level() {
    }

    public Level(String name) {
        this.name = name;
    }

    /**
     * 文物级别id
     */
    @Id
    private String id;

    /**
     * 文物级别名称
     */
    private String name;

    /**
     * 获取文物级别id
     *
     * @return id - 文物级别id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置文物级别id
     *
     * @param id 文物级别id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取文物级别名称
     *
     * @return name - 文物级别名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置文物级别名称
     *
     * @param name 文物级别名称
     */
    public void setName(String name) {
        this.name = name;
    }
}