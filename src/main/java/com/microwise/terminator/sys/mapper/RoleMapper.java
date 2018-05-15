package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 *
 * @author lijianfei
 * @date 2017-08-25
 */
@Mapper
public interface RoleMapper extends TerminatorMapper<Role> {

    /**
     * 查询用户角色列表
     *
     * @param userId 用户ID
     * @return
     */
    List<Role> findRoles(@Param("userId") String userId);

    /***
     *
     * @param role
     * @return
     *
     * 维护角色和菜单中间表关系
     */
    int insertRoleMenu(Role role);

    int deleteRoleMenu(Role role);

    /**
     * 通过角色id查询角色
     *
     * @param id
     * @return
     */
    Role find(@Param("id") String id);

    /**
     * 查询所有未被删除的角色
     *
     * @return
     */
    List<Role> findAll();
}