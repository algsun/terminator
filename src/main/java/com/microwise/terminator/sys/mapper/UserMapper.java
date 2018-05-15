package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper
 *
 * @author lijianfei
 * @version 2017-08-30
 */
@Mapper
public interface UserMapper extends TerminatorMapper<User> {
    /**
     * 校验重复用户
     *
     * @param condition
     * @param id
     * @return
     */
    List<User> findUser(@Param("condition") String condition, @Param("id") String id);

}