package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.FileUtils;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.*;
import com.microwise.terminator.sys.mapper.*;
import com.microwise.terminator.sys.util.TattletaleAPI;
import com.microwise.terminator.sys.util.UserUtils;
import com.microwise.terminator.sys.util.WindTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Relic service
 *
 * @author bai.weixing
 * @since 2017/9/20.
 */
@Service
@Transactional
public class RelicService extends CrudService<RelicMapper, Relic> {

    /**
     * 文物照片路径
     */
    private static final String PHOTO_PATH = "relic" + File.separator + "images" + File.separator;

    @Autowired
    private EraMapper eraMapper;
    @Autowired
    private LevelMapper levelMapper;
    @Autowired
    private TextureMapper textureMapper;
    @Autowired
    private PhotoMapper photoMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TattletaleAPI tattletaleAPI;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AlarmHistoryService alarmHistoryService;

    @Value("${terminator.imagesPath}")
    private String imagesPath;

    /**
     * 查询文物及监测数据
     *
     * @param name
     * @return
     * @throws Exception
     */
    public List<Relic> getRelicAndMonitorData(String name) throws Exception {
        List<Relic> relics = this.findRelicList(name, null);
        if (relics.isEmpty()) {
            return null;
        }
        for (Relic relic : relics) {
            relic.setPhoto(photoMapper.selectByPrimaryKey(relic.getPhotoId()));
            int deviceCount = 0, alarmCount = 0;
            Map<String, Object> count = Maps.newHashMap();
            List<Device> deviceList = deviceService.findDeviceListByRelicId(relic.getId());
            List<AlarmRecord> alarmRecordList = this.findAlarmRecord(relic.getId());
            if (deviceList != null && !deviceList.isEmpty()) {
                for (Device device : deviceList) {
                    if (device.getAnomaly() == 0) {
                        continue;
                    }
                    deviceCount++;
                }
            }
            if (alarmRecordList != null && !alarmRecordList.isEmpty()) {
                for (AlarmRecord alarmRecord : alarmRecordList) {
                    if (alarmRecord.getState() == 1) {
                        continue;
                    }
                    alarmCount++;
                }
            }
            count.put("deviceCount", deviceCount);
            count.put("alarmCount", alarmCount);
            relic.setDeviceList(deviceList);
            relic.setAlarmRecordList(alarmRecordList);
            relic.setCount(count);
        }
        return relics;
    }

    /**
     * 查找文物
     *
     * @param name    文物名称
     * @param relicId 文物id
     * @return
     */
    public List<Relic> findRelicList(String name, String relicId) {
        Example example = new Example(Relic.class);
        example.createCriteria().andEqualTo("delFlag", "0");
        example.and().andEqualTo("officeId", UserUtils.getCurrentUser().getOfficeId());
        if (StringUtils.isNotBlank(relicId)) {
            example.and().andEqualTo("id", relicId);
        }
        if (StringUtils.isNotBlank(name)) {
            example.and().andLike("name", "%" + name + "%");
        }
        List<Relic> relics = mapper.selectByExample(example);
        return relics;
    }


    /**
     * 查询报警点报警记录
     *
     * @param relicId 文物id
     * @return
     */
    public List<AlarmRecord> findAlarmRecord(String relicId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getAlarmPointRecordUrl());
        String paramUrl = builder.queryParam("sourceId", UserUtils.getCurrentUser().getOfficeId())
                .queryParam("alarmPointId", relicId)
                .queryParam("state", -1).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<List<AlarmRecord>>>() {
        };
        Result<List<AlarmRecord>> result = (Result<List<AlarmRecord>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        List<AlarmRecord> alarmRecordList = alarmHistoryService.buildAlarmRecord(result.getData());
        return alarmRecordList;
    }

    /**
     * 添加文物
     *
     * @param relic     文物
     * @param photoPath 图片路径
     */
    public void insert(Relic relic, String photoPath) {
        Photo photo = new Photo();
        photo.preInsert();
        photo.setPath(photoPath);
        photoMapper.insertSelective(photo);
        relic.setPhotoId(photo.getId());
        relic.preInsert();
        mapper.insertSelective(relic);
    }

    /**
     * 添加文物
     *
     * @param relic      文物
     * @param relicPhoto 文物图片
     * @throws Exception
     */
    public void insert(Relic relic, MultipartFile relicPhoto) throws Exception {
        if (!relicPhoto.isEmpty()) {
            String photoPath = FileUtils.doFile(relicPhoto, imagesPath);
            if (photoPath != null) {
                insert(relic, photoPath);
            }
        } else {
            relic.preInsert();
            mapper.insertSelective(relic);
        }
    }

    /**
     * 更新文物
     *
     * @param relic      文物
     * @param relicPhoto 文物照片
     * @return
     */
    public boolean update(Relic relic, MultipartFile relicPhoto, String operation) throws Exception {
        String oldPhotoId = relic.getPhotoId();
        String photoPath;
        //原来没有图片，现在也没有修改图片，图片依然为空，只更新文物信息
        //原来没有图片，现在添加新图片,图片不为空，更新文物信息，添加新图片
        if (StringUtils.isBlank(oldPhotoId)) {

            if (!relicPhoto.isEmpty()) {
                photoPath = FileUtils.doFile(relicPhoto, imagesPath);
                if (photoPath != null) {
                    Photo photo = new Photo();
                    photo.preInsert();
                    photo.setPath(photoPath);
                    photoMapper.insertSelective(photo);
                    relic.setPhotoId(photo.getId());
                } else {
                    throw new Exception("文物修改失败");
                }
            }
            relic.preUpdate();
            mapper.updateByPrimaryKeySelective(relic);
        } else {
            Photo oldPhoto = photoMapper.selectByPrimaryKey(oldPhotoId);
            String oldPath = oldPhoto.getPath();
            //原来有图片,现在删除了
            if (relicPhoto.isEmpty() && "remove".equals(operation)) {
                //更新照片信息
                oldPhoto.preUpdate();
                oldPhoto.setPath("");
                photoMapper.updateByPrimaryKeySelective(oldPhoto);
                //更新文物信息
                relic.preUpdate();
                relic.setPhotoId("");
                mapper.updateByPrimaryKeySelective(relic);
                //删除旧文物照片
                FileUtils.deleteFile(PHOTO_PATH + oldPath);
            }
            //原来有图片，现在没有修改
            if (relicPhoto.isEmpty() && "noChange".equals(operation)) {
                mapper.updateByPrimaryKeySelective(relic);
            }
            //原来有图片，现在修改成别的
            if (!relicPhoto.isEmpty()) {
                //上传新图片
                photoPath = FileUtils.doFile(relicPhoto, imagesPath);
                if (photoPath != null) {
                    oldPhoto.setPath(photoPath);
                    photoMapper.updateByPrimaryKeySelective(oldPhoto);
                    mapper.updateByPrimaryKeySelective(relic);
                } else {
                    throw new Exception("文物修改失败");
                }
            }
        }
        return true;
    }

    /**
     * 检查文物是否被使用
     *
     * @param id 文物id
     * @return
     */
    public boolean isUsed(String id) {
        boolean used = false;
        Location location = new Location();
        location.setZoneid(id);
        List<Location> locations = locationMapper.select(location);
        if (locations.size() > 0) {
            used = true;
        }
        return used;
    }

    /**
     * 删除文物
     *
     * @param id 文物id
     */
    public void delete(String id) throws Exception {
        Relic relic = mapper.selectByPrimaryKey(id);
        String photoId = relic.getPhotoId();
        Boolean flag = true;
        if (StringUtils.isNotBlank(photoId)) {
            Photo photo = photoMapper.selectByPrimaryKey(photoId);
            flag = FileUtils.deleteFile(PHOTO_PATH + photo.getPath());
        }
        if (flag) {
            relic.setDelFlag(true);
            mapper.updateByPrimaryKeySelective(relic);
        } else {
            throw new Exception("文物图片删除失败");
        }
    }

    /**
     * 根据文物名和id查询文物
     *
     * @param name 文物名称
     * @param id   文物id
     * @return
     */
    public List<Relic> findRelics(String name, String id) {
        String currentOfficeId = UserUtils.getCurrentUser().getOfficeId();
        Example example = new Example(Relic.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("delFlag", "0").andEqualTo("name", name).andEqualTo("officeId", currentOfficeId);
        if (StringUtils.isNotBlank(id)) {
            criteria.andNotEqualTo("id", id);
        }
        return mapper.selectByExample(example);
    }


    /**
     * 查询文物设备监测指标 实时数据并组装到实时数据对象（位置点存储）
     *
     * @param relicId              文物id
     * @param sensorPhysicalidList 监测指标过滤条件 （可以为null）
     * @return List<RealtimeDataVO> 实时数据vo列表
     * @author bai.weixing
     * @date 2017/10/12
     */
    private List<RealtimeDataVO> getRealtimeDataLocation(String relicId,
                                                         List<Integer> sensorPhysicalidList) {
        List<RealtimeDataVO> realtimeDataList = findLocationInfo(relicId, sensorPhysicalidList);
        List<RealtimeDataVO> realtimeDatas = new ArrayList<RealtimeDataVO>();
        for (RealtimeDataVO locationInfo : realtimeDataList) {
            List<LocationDataVO> locationDatas = locationService.findLocationSensor(
                    locationInfo.getLocationId(), sensorPhysicalidList);

            Map<Integer, LocationDataVO> sensorinfoMap = new HashMap<Integer, LocationDataVO>();
            for (LocationDataVO locationData : locationDatas) {
                if (locationData.getShowType() == 1) {
                    locationData.setSensorPhysicalValue(WindTools.updateWindDirection(locationData.getSensorPhysicalValue()));
                }
                sensorinfoMap.put(locationData.getSensorPhysicalid(),
                        locationData);
            }
            if (sensorinfoMap.size() > 0) {
                locationInfo.setLocationSensorInfoMap(sensorinfoMap);
                realtimeDatas.add(locationInfo);
            }

            if (locationDatas.size() > 0) {
                locationInfo.setStamp(locationDatas.get(0).getStamp());
            }
        }
        return realtimeDatas;
    }

    /**
     * 查询文物所有位置点实时状态
     *
     * @param relicId              文物id
     * @param sensorPhysicalidList 监测指标集合
     * @return
     */
    private List<RealtimeDataVO> findLocationInfo(String relicId,
                                                  List<Integer> sensorPhysicalidList) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("relicId", relicId);
        paramMap.put("sensorPhysicalidList", sensorPhysicalidList);
        return mapper.findLocationInfo(paramMap);
    }

    /**
     * 查询文物位置点实时数据
     *
     * @param relicId
     * @return
     */
    public List<RealtimeDataVO> findRealtimeDataLocation(String relicId) {
        return getRealtimeDataLocation(relicId, null);
    }

    /**
     * 查询文物拥有的已经激活的监测指标
     *
     * @param relicId
     * @return
     */
    public List<Sensorinfo> findSensorinfo(String relicId) {
        return mapper.findSensorinfo(relicId);
    }

    /**
     * 根据文物名称查询文物（带分页）
     *
     * @param name
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageInfo<Relic> findRelicsByName(String name, int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Example example = new Example(Relic.class);
        String currentOfficeId = UserUtils.getCurrentUser().getOfficeId();
        Example.Criteria criteria = example.createCriteria().andEqualTo("officeId", currentOfficeId);

        if (StringUtils.isNotBlank(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        criteria.andEqualTo("delFlag", 0);
        example.setOrderByClause("update_date desc");
        List<Relic> relics = mapper.selectByExample(example);
        for (Relic relic : relics) {
            relic.setLevel(levelMapper.selectByPrimaryKey(relic.getLevelId()));
            relic.setEra(eraMapper.selectByPrimaryKey(relic.getEraId()));
            relic.setTexture(textureMapper.selectByPrimaryKey(relic.getTextureId()));
        }
        return new PageInfo<>(relics);
    }

    /**
     * 根据id查询文物
     *
     * @param id
     * @return
     */
    public Relic findById(String id) {
        Relic relic = mapper.selectByPrimaryKey(id);
        relic.setLevel(levelMapper.selectByPrimaryKey(relic.getLevelId()));
        relic.setEra(eraMapper.selectByPrimaryKey(relic.getEraId()));
        relic.setTexture(textureMapper.selectByPrimaryKey(relic.getTextureId()));
        return relic;
    }


}
