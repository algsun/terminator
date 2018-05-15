package com.microwise.terminator.sys.mapper;

import com.microwise.terminator.common.persistence.TerminatorMapper;
import com.microwise.terminator.sys.entity.Location;
import com.microwise.terminator.sys.entity.LocationDataVO;
import com.microwise.terminator.sys.entity.RecentDataVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface LocationMapper extends TerminatorMapper<Location> {
    /**
     * 根据条件查询历史数据按数据创建时间倒序排列
     *
     * @param paramMap
     * @return
     */
    List<RecentDataVO> findHistoryDataList(Map<String, Object> paramMap);

    /**
     * 根据条件查询历史数据数量
     *
     * @param paramMap
     * @return
     */
    int findHistoryDataListCount(Map<String, Object> paramMap);

    /**
     * 根据条件查询历史数据按数据创建时间顺序排列
     *
     * @param paramMap
     * @return
     */
    List<RecentDataVO> findRecentDataList(Map<String, Object> paramMap);

    /**
     * 根据条件查询位置点数据
     *
     * @param paramMap
     * @return
     */
    List<LocationDataVO> findLocationHistoryData(Map<String, Object> paramMap);

    /**
     * 查询一定时间段的位置点历史数据数量
     *
     * @param paramMap
     * @return
     */
    int findLocationHistoryDataCount(Map<String, Object> paramMap);

    /**
     * 查询当前时间段有数据的年
     *
     * @param paramMap
     * @return
     */
    List<Integer> getExcelSum(Map<String, Object> paramMap);

    /**
     * 查询最大和最小时间为查询设备历史数据表做查询条件
     *
     * @param paramMap
     * @return
     */
    Map<String, Date> findMaxAndMinTime(Map<String, Object> paramMap);

    /**
     * 查找当前机构下最大的位置点
     *
     * @param siteId
     * @return
     */
    String getMaxLocationId(String siteId);

    /**
     * 查询一个位置点已经激活的检测指标对应的实时数据值
     *
     * @param paramMap
     * @return
     */
    List<LocationDataVO> findLocationSensor(Map<String, Object> paramMap);

    /**
     * 创建位置点数据表
     *
     * @param locationId
     */
    void createLocationTable(String locationId);

    /**
     * 删除位置点历史数据表
     *
     * @param locationId
     */
    void deleteLocationTable(String locationId);

    /**
     * 位置点修改
     *
     * @param location
     */
    void updateLocation(Location location);
}