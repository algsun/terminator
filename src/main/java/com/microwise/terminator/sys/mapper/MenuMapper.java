package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限菜单
 *
 * @author lijianfei
 * @since 2017-08-28
 */
@Mapper
public interface MenuMapper extends TerminatorMapper<Menu> {


    /**
     * 查询用户拥有的权限列表
     *
     * @param userId
     * @return
     */
    List<Menu> findByUserId(@Param("userId") String userId);

    /**
     * 查询所有权限列表
     *
     * @return
     */
    List<Menu> findAllList();
}