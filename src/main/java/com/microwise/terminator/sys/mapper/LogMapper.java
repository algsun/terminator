package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Log;
import org.apache.ibatis.annotations.Mapper;

/**
 * 日志 Mapper
 *
 * @author lijianfei
 * @version 2017-08-30
 */
@Mapper
public interface LogMapper extends TerminatorMapper<Log> {
}