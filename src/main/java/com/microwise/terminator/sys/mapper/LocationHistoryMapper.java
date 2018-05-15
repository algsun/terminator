package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.LocationHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LocationHistoryMapper extends TerminatorMapper<LocationHistory> {
    /**
     * 删除位置点历史记录
     *
     * @param locationId
     */
    void deleteLocationHistory(@Param("locationId") String locationId);
}