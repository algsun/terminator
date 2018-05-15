package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeviceMapper extends TerminatorMapper<Device> {
    /**
     * 根据设备id查询设备信息
     *
     * @param id
     * @return
     */
    Device findDeviceById(@Param("id") String id);

    /**
     * 根据设备id修改设备时间间隔
     *
     * @param nodeid
     */
    void updateIntervalByNodeId(@Param("nodeid") String nodeid, @Param("interval") Integer interval);

    /**
     * 根据设备id修改振动灵敏度
     *
     * @param nodeid
     * @param sensitivity
     */
    void updateSensitivityByNodeId(@Param("nodeid") String nodeid, @Param("sensitivity") Integer sensitivity);

    /**
     * 解除设备与位置点的绑定关系
     *
     * @param nodeid
     */
    void unBindLocation(@Param("nodeid") String nodeid);

    /**
     * 根据设备id删除设备状态信息
     *
     * @param nodeid
     */
    void deleteNodeInfoMemoryByNodeId(@Param("nodeid") String nodeid);

    /**
     * 根据设备id删除设备实时数据
     *
     * @param nodeid
     */
    void deleteNodeSensorByNodeId(@Param("nodeid") String nodeid);
}