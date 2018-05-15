package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Device;
import com.microwise.terminator.sys.entity.NodeInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NodeInfoMapper extends TerminatorMapper<NodeInfo> {
    /**
     * 查询所有设备
     *
     * @param nodeInfo
     * @return
     */
    List<Device> findDeviceList(NodeInfo nodeInfo);

    /**
     * 查询异常设备（超时、低电、掉电）
     * @return
     */
    List<Device> findAbnormalDevices();

    /**
     * 查询所有未绑定位置点的设备
     *
     * @return
     */
    List<NodeInfo> findUnbindDevices();
}