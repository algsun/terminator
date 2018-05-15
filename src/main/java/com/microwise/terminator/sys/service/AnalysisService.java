package com.microwise.terminator.sys.service;

import com.google.common.collect.Lists;
import com.microwise.terminator.sys.entity.*;
import com.microwise.terminator.sys.util.DateTimeUtil;
import com.microwise.terminator.sys.util.TattletaleAPI;
import com.microwise.terminator.sys.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计分析Service
 *
 * @author sun.cong
 * @create 2017-11-29 10:24
 **/
@Service
public class AnalysisService {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private RelicService relicService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TattletaleAPI tattletaleAPI;
    @Autowired
    private AlarmHistoryService alarmHistoryService;

    /**
     * 查询日报统计项
     *
     * @return
     */
    public Map<String, Object> getDailyReport() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getDailyReportUrl());
        String paramUrl = builder.queryParam("sourceId", UserUtils.getCurrentUser().getOfficeId())
                .queryParam("userId", UserUtils.getCurrentUser().getId())
                .queryParam("begin", DateTimeUtil.format("yyyy-MM-dd", DateTimeUtil.getYesterday()))
                .queryParam("end", DateTimeUtil.format("yyyy-MM-dd", new Date())).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<Map<String, Object>>>() {
        };
        Result<Map<String, Object>> result = (Result<Map<String, Object>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        return result.getData();
    }

    /**
     * 查询报警记录
     *
     * @param state
     * @return
     */
    public List<AlarmRecord> getAlarmRecords(int state) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getReportAlarm());
        String paramUrl = builder.queryParam("sourceId", UserUtils.getCurrentUser().getOfficeId())
                .queryParam("state", state).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<List<AlarmRecord>>>() {
        };
        Result<List<AlarmRecord>> result = (Result<List<AlarmRecord>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        List<AlarmRecord> alarmRecordList = alarmHistoryService.buildAlarmRecord(result.getData());
        return alarmRecordList;
    }

    /**
     * 设备详情
     *
     * @param anomaly
     * @return
     */
    public List<Device> findDeviceDetail(int anomaly) throws Exception {
        List<Device> devices = Lists.newArrayList();
        NodeInfo nodeInfo = new NodeInfo();
        nodeInfo.setSiteid(UserUtils.getCurrentUser().getOfficeId());
        nodeInfo.setNormalFlag(anomaly);
        List<Device> deviceList = deviceService.findDeviceList(nodeInfo);
        for (Device device : deviceList) {
            Office office = officeService.findOffice(Integer.parseInt(device.getSiteid()));
            Location location = locationService.findLocationByNodeId(device.getNodeid());
            Relic relic = null;
            if (location != null) {
                relic = relicService.get(location.getZoneid());
            }
            device.setOffice(office);
            device.setLocation(location);
            device.setRelic(relic);
            devices.add(device);
        }
        return devices;
    }
}
