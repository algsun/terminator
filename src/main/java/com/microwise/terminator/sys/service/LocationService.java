package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.config.Global;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.*;
import com.microwise.terminator.sys.mapper.*;
import com.microwise.terminator.sys.util.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 位置点Service
 *
 * @author sun.cong
 * @create 2017-09-21 13:55
 **/
@Service
@Transactional
public class LocationService extends CrudService<LocationMapper, Location> {
    @Autowired
    private RelicMapper relicMapper;
    @Autowired
    private RelicService relicService;

    @Autowired
    private SensorinfoMapper sensorinfoMapper;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private BPHttpApiClient bpHttpApiClient;

    @Autowired
    private LocationHistoryMapper locationHistoryMapper;

    @Autowired
    private LocationSensorMapper locationSensorMapper;

    Map<String, Object> paramMap = new HashedMap();


    /**
     * 查找文物下所有的位置点
     *
     * @param relicId 文物id
     * @return
     */
    public List<Location> findLocationListByRelicId(String relicId) {
        Example example = new Example(Location.class);
        example.createCriteria().andEqualTo("zoneid", relicId);
        return mapper.selectByExample(example);
    }

    /**
     * 查找设备绑定的位置点
     *
     * @param nodeid
     * @return
     */
    public Location findLocationByNodeId(String nodeid) throws Exception {
        Example example = new Example(Location.class);
        example.createCriteria().andEqualTo("nodeid", nodeid);
        List<Location> locations = mapper.selectByExample(example);
        if (locations.size() != 0) {
            return locations.get(0);
        }
        return null;
    }

    /**
     * 查找位置点
     *
     * @param location
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Location> findLocationList(Location location, int pageNum, int pageSize) throws Exception {
        PageHelper.startPage(pageNum, pageSize);
        Example example = new Example(Location.class);
        example.setOrderByClause("`createTime` DESC");
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(location.getLocationname())) {
            criteria.andLike("locationname", "%" + location.getLocationname() + "%");
        }
        if (location.getRelic() != null && StringUtils.isNotBlank(location.getRelic().getName())) {
            List<String> relicIds = new ArrayList<>();
            for (Relic relic : relicService.findRelics(location.getRelic().getName(), null)) {
                relicIds.add(relic.getId());
            }
            criteria.andIn("zoneid", relicIds);
        }
        List<Location> locationList = mapper.selectByExample(example);
        if (locationList.size() != 0) {
            for (Location locationInfo : locationList) {
                locationInfo.setRelic(relicMapper.selectByPrimaryKey(locationInfo.getZoneid()));
            }
        }
        return new PageInfo(locationList);
    }

    /**
     * 根据Id查找位置点
     *
     * @param location
     * @return
     */
    public Location findLocationById(Location location) throws Exception {
        if (StringUtils.isBlank(location.getId())) {
            return null;
        }
        return super.get(location.getId());
    }

    /**
     * 判断位置点名称是否重复
     *
     * @param name
     * @return
     */
    public Location findLocationByName(String name, String id) throws Exception {
        Example example = new Example(Location.class);
        example.createCriteria().andNotEqualTo("id", id);
        example.and().andEqualTo("locationname", name);
        List<Location> locationList = mapper.selectByExample(example);
        if (locationList.size() != 0) {
            return locationList.get(0);
        }
        return null;
    }

    /**
     * 增加位置点
     *
     * @param location
     */
    public void save(Location location) throws Exception {
        String locationId = this.getNewLocationId(UserUtils.getCurrentUser().getOfficeId());
        mapper.createLocationTable(locationId);
        location.setSiteid(UserUtils.getCurrentUser().getOfficeId());
        location.setId(locationId);
        location.setCreatetime(new Date());
        if (location.getNodeid() != null && location.getNodeid().equals("")) {
            location.setNodeid(null);
        }
        if (location.getZoneid() != null && location.getZoneid().equals("")) {
            location.setZoneid(null);
        }
        this.insertSelective(location);
        if (location.getNodeid() != null && !location.getNodeid().equals("")) {//增加位置点历史数据
            LocationHistory locationHistory = new LocationHistory();
            locationHistory.setLocationid(locationId);
            locationHistory.setNodeid(location.getNodeid());
            locationHistory.setStarttime(new Date());
            locationHistoryMapper.insertSelective(locationHistory);
        }
    }

    /**
     * 修改位置点
     *
     * @param location
     * @throws Exception
     */
    public List<String> updateLocation(Location location) throws Exception {
        if (location.getNodeid() != null && location.getNodeid().equals("")) {
            location.setNodeid(null);
        }
        if (location.getZoneid() != null && location.getZoneid().equals("")) {
            location.setZoneid(null);
        }
        mapper.updateLocation(location);
        List<String> deviceIds = new ArrayList<>();
        Example example = new Example(LocationHistory.class);
        example.createCriteria().andEqualTo("locationid", location.getId());
        example.setOrderByClause("`startTime` DESC");
        List<LocationHistory> locationHistories = locationHistoryMapper.selectByExample(example);
        if (locationHistories.size() == 0) {
            if (location.getNodeid() != null && !location.getNodeid().equals("")) {//新建绑定关系
                LocationHistory locationHistory = new LocationHistory();
                locationHistory.setLocationid(location.getId());
                locationHistory.setNodeid(location.getNodeid());
                locationHistory.setStarttime(new Date());
                locationHistoryMapper.insertSelective(locationHistory);
                deviceIds.add(location.getNodeid());
            }
        } else {
            //最后一条历史记录
            LocationHistory lastLocationHistory = locationHistories.get(0);
            if ((location.getNodeid() == null || location.getNodeid().equals(""))
                    && lastLocationHistory.getEndtime() == null) {//解除绑定
                lastLocationHistory.setEndtime(new Date());
                locationHistoryMapper.updateByPrimaryKey(lastLocationHistory);
                deviceIds.add(lastLocationHistory.getNodeid());
            }
            if (location.getNodeid() != null && !location.getNodeid().equals("")
                    && !location.getNodeid().equals(lastLocationHistory.getNodeid())) {//更改绑定关系
                lastLocationHistory.setEndtime(new Date());
                locationHistoryMapper.updateByPrimaryKey(lastLocationHistory);
                LocationHistory locationHistory = new LocationHistory();
                locationHistory.setLocationid(location.getId());
                locationHistory.setNodeid(location.getNodeid());
                locationHistory.setStarttime(new Date());
                locationHistoryMapper.insertSelective(locationHistory);
                deviceIds.add(location.getNodeid());
                deviceIds.add(lastLocationHistory.getNodeid());
            }
            if (lastLocationHistory.getEndtime() != null && location.getNodeid() != null
                    && !location.getNodeid().equals("") && location.getNodeid().equals(lastLocationHistory.getNodeid())) {
                LocationHistory locationHistory = new LocationHistory();
                locationHistory.setLocationid(location.getId());
                locationHistory.setNodeid(location.getNodeid());
                locationHistory.setStarttime(new Date());
                locationHistoryMapper.insertSelective(locationHistory);
                deviceIds.add(location.getNodeid());
            }
        }
        return deviceIds;
    }

    /**
     * 删除位置点
     *
     * @param location
     * @throws Exception
     */
    public String deleteLocation(Location location) throws Exception {
        Location locationDetail = findLocationById(location);
        locationSensorMapper.deleteLocationSensor(location.getId());
        locationHistoryMapper.deleteLocationHistory(location.getId());
        this.delete(location);
        mapper.deleteLocationTable(location.getId());
        return locationDetail.getNodeid();
    }

    /**
     * 通知中间件位置点发生变化
     *
     * @param deviceId 设备编号
     */
    public void notifyLocationChanged(String deviceId) throws Exception {
        if (StringUtils.isBlank(deviceId)) {
            return;
        }
        Map<String, Object> map = bpHttpApiClient.notifyLocationChanged(deviceId);
        if (!(Boolean) map.get("success")) {
            throw new NotifyLocationChangedException("通知中间件位置点和设备关系变化时发生异常");
        }
    }

    /**
     * 获取位置点ID
     *
     * @param siteId
     * @return
     */
    public String getNewLocationId(String siteId) throws Exception {
        String locationId = mapper.getMaxLocationId(siteId);
        if (StringUtils.isNotBlank(locationId)) {
            locationId = String.valueOf(Long.parseLong(locationId) + 1);
        } else {
            locationId = siteId.concat(Global.DEFAULT_FIRST_LOCATIONID);
        }
        return locationId;
    }

    /**
     * 自定义通知中间件通讯异常
     */
    class NotifyLocationChangedException extends RuntimeException {
        public NotifyLocationChangedException(String message) {
            super(message);
        }
    }

    public Location findLocationById(String locationId) {
        Location location = this.get(locationId);
        if (location == null) return null;
        location.setSensorinfoList(sensorinfoMapper.findSensorInfoList(locationId));
        location.setDevice(deviceMapper.findDeviceById(location.getNodeid()));
        location.setRelic(relicMapper.selectByPrimaryKey(location.getRelic()));
        return location;
    }

    public PageInfo<RecentDataVO> findHistoryDataList(String locationId, Date startDate, Date endDate, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        List<RecentDataVO> recentDataList = mapper.findHistoryDataList(paramMap);
        //无数据直接返回
        if (recentDataList.isEmpty()) {
            return new PageInfo<>(recentDataList);
        }
        //组装数据
        assembRealtimeData(locationId, recentDataList);
        return new PageInfo<>(recentDataList);
    }

    public int findHistoryDataListCount(String locationId, Date beginTime, Date endTime) {
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("beginTime", beginTime);
        paramMap.put("endTime", endTime);
        return mapper.findHistoryDataListCount(paramMap);
    }

    public List<LocationDataVO> findLocationHistoryData(String locationId, Date date) {
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("date", date);
        return mapper.findLocationHistoryData(paramMap);
    }

    /**
     * 组装位置点实时数据
     *
     * @param locationId       位置点id
     * @param recentDataVOList 位置点实时数据对象列表
     */
    private void assembRealtimeData(String locationId, List<RecentDataVO> recentDataVOList) {
        List<LocationDataVO> locationDataList;
        //根据位置点id、最小时间、最大时间获取位置点数据
        for (RecentDataVO recentData : recentDataVOList) {
            Date date = recentData.getStamp();
            locationDataList = findLocationHistoryData(locationId, date);
            Map<Integer, LocationDataVO> locationDataMap = new HashMap<Integer, LocationDataVO>();
            for (LocationDataVO locationDataVO : locationDataList) {
                if (locationDataVO.getShowType() == 1) {
                    locationDataVO.setSensorPhysicalValue(WindTools
                            .updateWindDirection(locationDataVO.getSensorPhysicalValue()));
                }
                locationDataMap.put(locationDataVO.getSensorPhysicalid(), locationDataVO);
            }
            recentData.setSensorInfoMap(locationDataMap);
        }
    }


    public String getFileName(String locationId, Date startTime, Date endTime) {
        String fileName = "";
        int startYear = new DateTime(startTime).getYear();// 开始年
        int endYear = new DateTime(endTime).getYear(); // 结束年
        Location location = mapper.selectByPrimaryKey(locationId);
        String locationName = location.getLocationname();
        // 判断开始年与结束年是否相等 相等直接生成excel文件名
        if (endYear == startYear) {
            fileName = ExportUtil.getFileName(locationName, locationId, startTime,
                    endTime, false);
        } else {
            paramMap.clear();
            paramMap.put("locationId", locationId);
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
            List<Integer> yearList = mapper.getExcelSum(paramMap);
            // 只有一年有数据
            if (yearList.size() == ExportUtil.FILR_TYPE_EXCEL) {
                // 获得有数据年的最大时间和最小时间
                Map<String, Date> dateMap = findMaxAndMinTime(
                        locationId, new DateTime(yearList.get(0), 1, 1, 0, 0, 0)
                                .toDate(), new DateTime(yearList.get(0), 12,
                                31, 23, 59, 59).toDate(), null, null);
                fileName = ExportUtil.getFileName(locationName, locationId,
                        dateMap.get("minTime"),
                        dateMap.get("maxTime"), false);
            } else {
                fileName = ExportUtil.getFileName(locationName, locationId,
                        startTime, endTime, true);
            }
        }
        return fileName;
    }


    public void exportHistoryData(String locationId, Date startTime, Date endTime, OutputStream outputStream) throws Exception {
        Location location = findLocationById(locationId);
        String locationName = location.getLocationname();
        // 获得excel 列头
        List<Sensorinfo> sensorinfoList = location.getSensorinfoList();
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        List<Integer> yearList = mapper.getExcelSum(paramMap);
        Workbook workbook = null;
        ZipOutputStream zos = null;
        // 如果只有一年有数据 生成workbook并 写入到流中
        if (yearList.size() == ExportUtil.FILR_TYPE_EXCEL) {
            workbook = createWorkbook(locationName, locationId, sensorinfoList,
                    ExportUtil.getMinTime(yearList.get(0), startTime),
                    ExportUtil.getMaxTime(yearList.get(0), endTime));
            workbook.write(outputStream);
        } else {
            // 多年有数据调用 导出zip 包的方法
            exportZip(outputStream, zos, yearList, startTime, endTime,
                    locationName, locationId, sensorinfoList);
        }
    }


    private Workbook createWorkbook(String deviceName, String locationId,
                                    List<Sensorinfo> sensorinfoList, DateTime startTime,
                                    DateTime endTime) {
        Workbook workbook = new SXSSFWorkbook();
        int currentMonth = startTime.getMonthOfYear(); // 当前月份
        // 当当前月大于结束月时结束while循环
        while (endTime.getMonthOfYear() >= currentMonth) {
            createSheet(workbook, currentMonth, startTime, endTime, deviceName,
                    locationId, sensorinfoList);

            currentMonth++;
        }
        return workbook;
    }

    public void createSheet(Workbook workbook, int currentMonth,
                            DateTime startTime, DateTime endTime, String locationName,
                            String locationId, List<Sensorinfo> sensorinfoList) {
        int year = startTime.getYear(); // 获得当前年份
        int startMonth = startTime.getMonthOfYear(); // 开始月份
        int endMonth = endTime.getMonthOfYear(); // 结束月份
        // 创建sheet,计算 每个sheet 中历史数据的开始时间和结束时间
        DateTime minTime = new DateTime(startTime.getYear(), currentMonth, 1,
                0, 0, 0);
        // 用来获得当前月的最大天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateTimeUtil.parseUncheck(DateTimeUtil.YYYY_MM_DD, year + "-"
                + currentMonth + "-01"));

        DateTime maxTime = new DateTime(endTime.getYear(), currentMonth,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        if (currentMonth == startMonth) {
            minTime = startTime;
        }
        if (currentMonth == endMonth) {
            maxTime = endTime;
        }
        // 判断当前月有没有数据
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("startTime", minTime.toDate());
        paramMap.put("endTime", maxTime.toDate());
        int count = mapper.findLocationHistoryDataCount(paramMap);
        if (count > 0) {
            int sheets = pagesCount(count, 60000);
            for (int i = 1; i < sheets + 1; i++) {
                Sheet sheet = workbook.createSheet(currentMonth + "月份" + (sheets > 1 ? "-" + i : ""));
                // 获得当前月的历史数据
                List<RecentDataVO> historyDataList = findRecentDataList(locationId, minTime.toDate(), maxTime.toDate(), i, 60000);
                Map<String, Date> dateMap = findMaxAndMinTime(locationId,
                        minTime.toDate(), maxTime.toDate(), null, null);
                String sheetTitle = ExportUtil.getFileName(locationName, locationId,
                        dateMap.get("minTime"),
                        dateMap.get("maxTime"), false);
                new ExcelUtil().assembleLocationSheet(sensorinfoList, historyDataList, sheet,
                        sheetTitle.substring(0, sheetTitle.length() - 4));
            }
        }
    }

    public int pagesCount(int count, int sizePerPage) {
        int pageCount = count / sizePerPage;
        if (count % sizePerPage > 0) {
            ++pageCount;
        }
        return pageCount;
    }


    /**
     * 导出zip
     *
     * @param outputStream   输出流
     * @param zos            zip输出流
     * @param yearList       有数据的年份列表
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param locationName   位置点名称
     * @param locationId     位置点Id
     * @param sensorinfoList 监测指标表列
     * @author xuyuexi
     * @date 2014-7-8
     */
    public void exportZip(OutputStream outputStream, ZipOutputStream zos,
                          List<Integer> yearList, Date startTime, Date endTime,
                          String locationName, String locationId,
                          List<Sensorinfo> sensorinfoList) throws IOException {
        for (Integer integer : yearList) {
            DateTime minTime = ExportUtil.getMinTime(integer, startTime);
            DateTime maxTime = ExportUtil.getMaxTime(integer, endTime);
            Workbook workbook = createWorkbook(locationName, locationId,
                    sensorinfoList, minTime, maxTime);
            if (zos == null) {
                zos = new ZipOutputStream(outputStream);
            }
            // 将workbook 写入流中
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byte[] arrayByte = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            Map<String, Date> dateMap = findMaxAndMinTime(locationId,
                    minTime.toDate(), maxTime.toDate(), null, null);
            ZipEntry ze = new ZipEntry(ExportUtil.getFileName(locationName,
                    locationId, dateMap.get("minTime"),
                    dateMap.get("maxTime"), false));
            zos.putNextEntry(ze);
            zos.write(arrayByte);
        }
        zos.close();

    }

    public Map<String, Date> findMaxAndMinTime(String locationId, Date startTime, Date endTime, Integer index, Integer pageSize) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("locationId", locationId);
        paramMap.put("endTime", endTime);
        if (index != null && pageSize != null) {
            paramMap.put("start", (index - 1) * pageSize);
            paramMap.put("pageSize", pageSize);
        }
        paramMap.put("startTime", startTime);
        return mapper.findMaxAndMinTime(paramMap);


    }

    public List<RecentDataVO> findRecentDataList(String locationId, Date startDate, Date endDate, int page, int pageSize) {
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("startTime", startDate);
        paramMap.put("endTime", endDate);
        int begin = (page - 1) * 60000;
        paramMap.put("begin", begin);
        paramMap.put("pageSize", 60000);
        List<RecentDataVO> recentDataList = mapper.findRecentDataList(paramMap);
        //无数据直接返回
        if (recentDataList.isEmpty()) {
            return recentDataList;
        }

        assembRealtimeData(locationId, recentDataList);
        return recentDataList;
    }

    /**
     * 获得位置点监测指标实时数据
     *
     * @param locationId           位置点id
     * @param sensorPhysicalidList 监测指标集合
     * @return
     */
    public List<LocationDataVO> findLocationSensor(String locationId, List<Integer> sensorPhysicalidList) {
        paramMap.clear();
        paramMap.put("locationId", locationId);
        paramMap.put("sensorPhysicalIdList", sensorPhysicalidList);
        return mapper.findLocationSensor(paramMap);
    }
}
