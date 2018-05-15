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
 * 知晓记录Service
 *
 * @author sun.cong
 * @create 2017-11-28 13:07
 **/
@Service
public class AwareRecordService {

    @Autowired
    private AlarmTaskService alarmTaskService;
    @Autowired
    private TattletaleAPI tattletaleAPI;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AlarmHistoryService alarmHistoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private RelicService relicService;

    /**
     * 添加知晓记录
     */
    public Boolean save() throws Exception {
        List<AwareRecord> awareRecordList = buildAwareRecordList();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.saveAwareRecordList());
        Result result = restTemplate.postForObject(builder.toUriString(), awareRecordList, Result.class);
        if (result.getCode() == TattletaleAPI.SUCCESS) {
            return true;
        }
        return false;
    }

    /**
     * 生成需要添加的知晓记录集合
     *
     * @return
     * @throws Exception
     */
    public List<AwareRecord> buildAwareRecordList() throws Exception {
        List<AwareRecord> awareRecords = Lists.newArrayList();
        User user = UserUtils.getCurrentUser();
        String officeId = user.getOfficeId();
        String userId = user.getId();
        PageInfo<AlarmRecord> pageInfo = alarmTaskService.alarmTasks(officeId, userId, 0, 0);
        List<AlarmRecord> taskList = pageInfo.getList();
        if (taskList.isEmpty()) {
            return awareRecords;
        }
        List<AwareRecord> awareRecordList = findAwareRecords(officeId, userId);
        if (awareRecordList.isEmpty()) {
            for (AlarmRecord alarmRecord : taskList) {
                AwareRecord awareRecord = new AwareRecord();
                awareRecord.setAlarmRecordId(alarmRecord.getId());
                awareRecord.setAwareNotifier(userId);
                awareRecords.add(awareRecord);
            }
            return awareRecords;
        }
        alarm:
        for (AlarmRecord alarmRecord : taskList) {
            String alarmRecordId = alarmRecord.getId();
            for (AwareRecord awareRecord : awareRecordList) {
                if (alarmRecordId.equals(awareRecord.getAlarmRecordId())) {
                    continue alarm;
                }
            }
            AwareRecord awareRecord = new AwareRecord();
            awareRecord.setAlarmRecordId(alarmRecordId);
            awareRecord.setAwareNotifier(userId);
            awareRecords.add(awareRecord);
        }
        return awareRecords;
    }

    /**
     * 查找知晓记录
     *
     * @param sourceId
     * @param userId
     * @return
     */
    public List<AwareRecord> findAwareRecords(String sourceId, String userId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getAwareRecordsUrl());
        String paramUrl = builder.queryParam("sourceId", sourceId).queryParam("userId", userId).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<List<AwareRecord>>>() {
        };
        Result<List<AwareRecord>> result = (Result<List<AwareRecord>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        return result.getData();
    }

    /**
     * 查找知晓记录(模糊查询)
     *
     * @param relicName
     * @param user
     * @param begin
     * @param end
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<AwareRecord> findAwareRecords(String relicName, String user, Date begin, Date end,
                                                  int pageNum, int pageSize) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getAwareRecordsBlurryUrl());
        List<String> relicIds = alarmHistoryService.buildAlarmPoints(relicName);
        List<String> userIds = this.buildUsers(user);
        if ((user != null && !user.isEmpty() && userIds.isEmpty()) || (relicName != null && !relicName.isEmpty() && relicIds.isEmpty())) {
            return null;
        }
        builder.queryParam("alarmPoints", relicIds.toArray())
                .queryParam("users", userIds.toArray());
        if (begin != null && end != null) {
            builder.queryParam("begin", DateTimeUtil.format("yyyy-MM-dd", begin))
                    .queryParam("end", DateTimeUtil.format("yyyy-MM-dd", end));
        }
        if (pageNum != 0 && pageSize != 0) {
            builder.queryParam("pageNum", pageNum)
                    .queryParam("pageSize", pageSize);
        }
        String paramUrl = builder.queryParam("sourceId", UserUtils.getCurrentUser().getOfficeId()).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<PageInfo<AwareRecord>>>() {
        };
        Result<PageInfo<AwareRecord>> result = (Result<PageInfo<AwareRecord>>) restTemplate.exchange(paramUrl, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        List<AwareRecord> awareRecords = result.getData().getList();
        result.getData().setList(this.buildAwareRecord(awareRecords));
        return result.getData();
    }

    /**
     * 生成知晓人id集合
     *
     * @param userName
     * @return
     */
    public List<String> buildUsers(String userName) {
        List<String> users = Lists.newArrayList();
        if (StringUtils.isNotBlank(userName)) {
            List<User> userList = userService.findUsersByName(userName);
            if (userList.isEmpty()) {
                return users;
            }
            for (User user : userList) {
                users.add(user.getId());
            }
        }
        return users;
    }

    /**
     * 填充知晓记录
     *
     * @param awareRecords
     * @return
     */
    public List<AwareRecord> buildAwareRecord(List<AwareRecord> awareRecords) {
        for (AwareRecord awareRecord : awareRecords) {
            AlarmRecord alarmRecord = awareRecord.getAlarmRecord();
            alarmRecord.setOffice(officeService.findOffice(Integer.parseInt(alarmRecord.getSourceid())));
            alarmRecord.setRelic(relicService.get(alarmRecord.getAlarmpointid()));
            awareRecord.setAlarmRecord(alarmRecord);
            awareRecord.setAwareUser(userService.get(awareRecord.getAwareNotifier()));
        }
        return awareRecords;
    }
}