package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.RealtimeDataVO;
import com.microwise.terminator.sys.entity.Relic;
import com.microwise.terminator.sys.entity.Sensorinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface RelicMapper extends TerminatorMapper<Relic> {
    /**
     * 查询文物所有位置点实时状态
     *
     * @param paramMap
     * @return
     */
    List<RealtimeDataVO> findLocationInfo(Map<String, Object> paramMap);

    /**
     * 查询文物拥有已经激活的监测指标
     *
     * @param relicId
     * @return
     */
    List<Sensorinfo> findSensorinfo(@Param("relicId") String relicId);
}