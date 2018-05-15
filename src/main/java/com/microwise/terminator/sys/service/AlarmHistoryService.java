package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.microwise.terminator.common.utils.StringUtils;
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

/**
 * 报警记录Service
 *
 * @author sun.cong
 * @create 2017-11-28 13:07
 **/
@Service
public class AlarmHistoryService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RelicService relicService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private AlarmTaskService alarmTaskService;
    @Autowired
    private AwareRecordService awareRecordService;
    @Autowired
    private TattletaleAPI tattletaleAPI;

    /**
     * 查询报警记录
     *
     * @param relicName
     * @param state
     * @param begin
     * @param end
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo<AlarmRecord> findAlarmHistoryList(String relicName, int state, Date begin, Date end,
                                                      int pageNum, int pageSize) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getAlarmRecordUrl());
        builder.queryParam("alarmPoints", this.buildAlarmPoints(relicName).toArray());
        if (begin != null && end != null) {
            builder.queryParam("begin", DateTimeUtil.format("yyyy-MM-dd", begin))
                    .queryParam("end", DateTimeUtil.format("yyyy-MM-dd", end));
        }
        if (pageNum != 0 && pageSize != 0) {
            builder.queryParam("pageNum", pageNum)
                    .queryParam("pageSize", pageSize);
        }
        String paramUrl = builder.queryParam("sourceId", UserUtils.getCurrentUser().getOfficeId())
                .queryParam("state", state).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<PageInfo<AlarmRecord>>>() {
        };
        Result<PageInfo<AlarmRecord>> result = (Result<PageInfo<AlarmRecord>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();

        List<AlarmRecord> alarmRecords = result.getData().getList();
        result.getData().setList(this.buildAlarmRecord(alarmRecords));
        return result.getData();
    }

    /**
     * 生成报警点id集合
     *
     * @param alarmPoint
     * @return
     */
    public List<String> buildAlarmPoints(String alarmPoint) {
        List<String> relicIds = Lists.newArrayList();
        if (StringUtils.isNotBlank(alarmPoint)) {
            List<Relic> relics = relicService.findRelicList(alarmPoint, null);
            if (relics.isEmpty()) {
                return relicIds;
            }
            for (Relic relic : relics) {
                relicIds.add(relic.getId());
            }
        }
        return relicIds;
    }

    /**
     * 填充报警记录
     *
     * @param alarmRecords
     * @return
     */
    public List<AlarmRecord> buildAlarmRecord(List<AlarmRecord> alarmRecords) {
        for (AlarmRecord alarmRecord : alarmRecords) {
            alarmRecord.setOffice(officeService.findOffice(Integer.parseInt(alarmRecord.getSourceid())));
            alarmRecord.setRelic(relicService.get(alarmRecord.getAlarmpointid()));
            if (StringUtils.isNotBlank(alarmRecord.getTransactor())) {
                alarmRecord.setUser(userService.get(alarmRecord.getTransactor()));
            }
            alarmRecord.setNotifiers();
        }
        return alarmRecords;
    }

    /**
     * 生成需要添加的知晓记录集合
     *
     * @return
     * @throws Exception
     */
    public List<AlarmRecord> buildLoginAlarm() throws Exception {
        List<AlarmRecord> alarmRecords = Lists.newArrayList();
        User user = UserUtils.getCurrentUser();
        String officeId = user.getOfficeId();
        String userId = user.getId();
        PageInfo<AlarmRecord> pageInfo = alarmTaskService.alarmTasks(officeId, userId, 0, 0);
        List<AlarmRecord> taskList = pageInfo.getList();
        if (taskList.isEmpty()) {
            return alarmRecords;
        }
        List<AwareRecord> awareRecordList = awareRecordService.findAwareRecords(officeId, userId);
        if (awareRecordList.isEmpty()) {
            return taskList;
        }
        alarm:
        for (AlarmRecord alarmRecord : taskList) {
            String alarmRecordId = alarmRecord.getId();
            for (AwareRecord awareRecord : awareRecordList) {
                if (alarmRecordId.equals(awareRecord.getAlarmRecordId())) {
                    continue alarm;
                }
            }
            alarmRecords.add(alarmRecord);
        }
        return alarmRecords;
    }

}