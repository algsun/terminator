package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Office;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OfficeMapper extends TerminatorMapper<Office> {
}