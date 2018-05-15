package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Sensorinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SensorinfoMapper extends TerminatorMapper<Sensorinfo> {
    /**
     * 根据位置点id查询传感信息
     *
     * @param locationId
     * @return
     */
    List<Sensorinfo> findSensorInfoList(@Param("locationId") String locationId);

    /**
     * 通过physicalId查找sensor名称
     *
     * @return
     */
    String findNameByPhysicalId(Integer id);

    /**
     * 根据传感量标识查询传感信息
     *
     * @param physicalIds
     * @return
     */
    List<Sensorinfo> findSensorinfos(@Param("physicalIds") List<String> physicalIds);
}