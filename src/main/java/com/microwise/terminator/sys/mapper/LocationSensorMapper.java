package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.LocationSensor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LocationSensorMapper extends TerminatorMapper<LocationSensor> {
    /**
     * 删除位置点实时数据
     *
     * @param locationId
     */
    void deleteLocationSensor(@Param("locationId") String locationId);
}