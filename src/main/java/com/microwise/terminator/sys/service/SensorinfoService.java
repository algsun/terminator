package com.microwise.terminator.sys.service;

import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.sys.entity.Sensorinfo;
import com.microwise.terminator.sys.mapper.SensorinfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 传感信息service
 *
 * @author bai.weixing
 * @since 2017/10/10.
 */
@Service
@Transactional
public class SensorinfoService extends CrudService<SensorinfoMapper, Sensorinfo> {
    /**
     * 根据physicalId查找传感量
     *
     * @param id
     * @return
     */
    public String findNameByPhysicalId(Integer id) {
        return mapper.findNameByPhysicalId(id);
    }


    public List<Sensorinfo> findSensorinfos(List<String> physicalIds) {
        return mapper.findSensorinfos(physicalIds);
    }
}
