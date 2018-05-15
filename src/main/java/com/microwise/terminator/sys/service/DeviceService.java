package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.Device;
import com.microwise.terminator.sys.entity.Location;
import com.microwise.terminator.sys.entity.NodeInfo;
import com.microwise.terminator.sys.entity.NodeSensor;
import com.microwise.terminator.sys.mapper.DeviceMapper;
import com.microwise.terminator.sys.mapper.LocationMapper;
import com.microwise.terminator.sys.mapper.NodeInfoMapper;
import com.microwise.terminator.sys.mapper.NodeSensorMapper;
import com.microwise.terminator.sys.util.BPHttpApiClient;
import com.microwise.terminator.sys.util.UserUtils;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.List;

/**
 * 设备Service
 *
 * @author sun.cong
 * @create 2017-09-20 14:45
 **/
@Service
@Transactional
public class DeviceService extends CrudService<NodeInfoMapper, NodeInfo> {
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private LocationService locationService;
    @Autowired
    private NodeSensorMapper nodeSensorMapper;

    @Autowired
    BPHttpApiClient client;

    /**
     * 查找文物下挂载的所有设备
     *
     * @param relicId
     * @return
     */
    public List<Device> findDeviceListByRelicId(String relicId) {
        List<Device> deviceList = Lists.newArrayList();
        List<Location> locationList = locationService.findLocationListByRelicId(relicId);
        if (locationList.isEmpty()) {
            return null;
        }
        for (Location location : locationList) {
            NodeInfo nodeInfo = new NodeInfo();
            nodeInfo.setNodeid(location.getNodeid());
            nodeInfo.setSiteid(UserUtils.getCurrentUser().getOfficeId());
            List<Device> devices = this.findDeviceList(nodeInfo);
            if (devices.isEmpty()) {
                continue;
            }
            Device device = devices.get(0);
            device.setLocation(location);
            deviceList.add(device);
        }
        return deviceList;
    }

    /**
     * 查询所有异常设备（超时，低电，掉电）
     *
     * @return
     */
    public List<Device> findAbnormalDevices() {
        return mapper.findAbnormalDevices();
    }

    /**
     * 查询所有设备
     *
     * @param nodeInfo
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Device> findDeviceList(NodeInfo nodeInfo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(mapper.findDeviceList(nodeInfo));
    }

    /**
     * 查询所有设备(不分页)
     *
     * @param nodeInfo
     * @return
     */
    public List<Device> findDeviceList(NodeInfo nodeInfo) {
        return mapper.findDeviceList(nodeInfo);
    }

    /**
     * 查找所有节点
     *
     * @return
     */
    public List<NodeInfo> findUnbindDevices(Location location) throws Exception {
        List<NodeInfo> nodeInfos = mapper.findUnbindDevices();
        if (location != null) {
            NodeInfo nodeInfo = findNodeInfoByLocation(location);
            if (nodeInfo != null) {
                nodeInfos.add(nodeInfo);
            }
        }
        return nodeInfos;
    }

    /**
     * 根据位置点查找设备
     *
     * @param location
     * @return
     * @throws Exception
     */
    private NodeInfo findNodeInfoByLocation(Location location) throws Exception {
        Location locationDetail = locationService.findLocationById(location);
        if (locationDetail.getNodeid() != null) {
            return mapper.selectByPrimaryKey(locationDetail.getNodeid());
        }
        return null;
    }

    /**
     * 查询设备详细信息
     *
     * @param nodeInfo
     * @return
     */
    public Device findDeviceDetails(NodeInfo nodeInfo) throws Exception {
        nodeInfo.setFlag(false);
        List<Device> devices = mapper.findDeviceList(nodeInfo);
        if (devices.size() != 0) {
            Device device = devices.get(0);
            //查找设备绑定的位置点
            if (StringUtils.isNotBlank(device.getNodeid())) {
                Location location = locationService.findLocationByNodeId(device.getNodeid());
                device.setLocation(location);
                Example example = new Example(NodeSensor.class);
                example.createCriteria().andEqualTo("nodeid", device.getNodeid());
                for (NodeSensor nodeSensor : nodeSensorMapper.selectByExample(example)) {
                    if (nodeSensor.getSensorphysicalid() == 114) {
                        device.setHasShake(true);
                    }
                }
            }
            return device;
        }
        return null;
    }

    /**
     * 修改设备
     *
     * @param device
     */
    public Boolean update(Device device) throws Exception {
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setNodeid(device.getNodeid());
        nodeInfo.setVoltagethreshold(device.getVoltagethreshold());
        mapper.updateByPrimaryKeySelective(nodeInfo);
        if (device.getIsIntervalChange()) {
            if (!modifyInterval(device)) {
                return false;
            }
            deviceMapper.updateIntervalByNodeId(device.getNodeid(), device.getInterval_i());
        }
        return true;
    }

    /**
     * 修改振动灵敏度
     *
     * @param device
     * @return
     * @throws Exception
     */
    public Boolean updateSensitivity(Device device) throws Exception {
        if (modifySensitivity(device)) {
            deviceMapper.updateSensitivityByNodeId(device.getNodeid(), device.getSensitivity());
            return true;
        }
        return false;
    }

    /**
     * 删除设备
     *
     * @param nodeid
     */
    public Boolean delete(String nodeid) throws Exception {
        if (client.deleteDevice(nodeid)) {
            deviceMapper.unBindLocation(nodeid);
            deviceMapper.deleteNodeSensorByNodeId(nodeid);
            deviceMapper.deleteNodeInfoMemoryByNodeId(nodeid);
            mapper.deleteByPrimaryKey(nodeid);
            return true;
        }
        return false;
    }

    /**
     * 修改设备时间间隔
     *
     * @param device
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private Boolean modifyInterval(Device device) throws Exception {
        return client.modifyInterval(device.getNodeid(), device.getInterval_i());
    }

    /**
     * 修改振动灵敏度
     *
     * @param device
     * @return
     * @throws Exception
     */
    public Boolean modifySensitivity(Device device) throws Exception {
        return client.modifySensitivity(device.getNodeid(), device.getSensitivity());
    }
}
