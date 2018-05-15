/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.microwise.terminator.common.service;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Service基类
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@Transactional
public abstract class CrudService<M extends Mapper<T>, T> {

    /**
     * 持久层对象
     */
    @Autowired
    protected M mapper;

    /**
     * 获取单条数据
     *
     * @param id
     * @return
     */
    public T get(String id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 获取单条数据
     *
     * @param entity
     * @return
     */
    public T get(T entity) {
        return mapper.selectOne(entity);
    }

    /**
     * 查询列表数据
     *
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return mapper.select(entity);
    }

    /**
     * 添加单条数据
     *
     * @param entity
     * @return
     */
    public int insert(T entity) {
        return mapper.insert(entity);
    }

    /**
     * 添加单条数据，忽略null
     *
     * @param entity
     * @return
     */
    public int insertSelective(T entity){ return mapper.insertSelective(entity);}

    /**
     * 根据主键更新不为空的数据
     *
     * @param entity
     * @return
     */
    public int update(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }


//    /**
//     * 查询分页数据
//     *
//     * @param page   分页对象
//     * @param entity
//     * @return
//     */
//    public Page<T> findPage(Page<T> page, T entity) {
//        entity.setPage(page);
//        page.setList(dao.findList(entity));
//        return page;
//    }
//
//    /**
//     * 保存数据（插入或更新）
//     *
//     * @param entity
//     */
//    @Transactional(readOnly = false)
//    public void save(T entity) {
//        if (entity.getIsNewRecord()) {
//            entity.preInsert();
//            dao.insert(entity);
//        } else {
//            entity.preUpdate();
//            dao.update(entity);
//        }
//    }

    /**
     * 真删除数据
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public void delete(T entity) {
        mapper.deleteByPrimaryKey(entity);
    }

}
