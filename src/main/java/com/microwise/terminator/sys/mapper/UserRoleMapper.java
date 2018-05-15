package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户角色关系 Mapper
 *
 * @author sun.cong
 * @version 2017-08-30
 */
@Mapper
public interface UserRoleMapper extends TerminatorMapper<UserRole> {
}