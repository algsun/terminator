package com.microwise.terminator.common.persistence;

import java.io.Serializable;

/**
 * 实体类抽象
 *
 * @author li.jianfei
 * @since 2017/9/6
 */
public abstract class BaseEntity<T> implements Serializable {

    /**
     * 插入之前执行方法，子类实现
     */
    public abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    public abstract void preUpdate();
}
