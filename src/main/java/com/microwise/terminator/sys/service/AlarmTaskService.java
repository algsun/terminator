package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.microwise.terminator.sys.entity.AlarmRecord;
import com.microwise.terminator.sys.entity.Relic;
import com.microwise.terminator.sys.entity.Result;
import com.microwise.terminator.sys.mapper.RelicMapper;
import com.microwise.terminator.sys.util.RestTemplateUtil;
import com.microwise.terminator.sys.util.TattletaleAPI;
import com.microwise.terminator.sys.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报警任务service.
 *
 * @author bai.weixing
 * @since 2017/11/28.
 */
@Service
@Transactional
public class AlarmTaskService {



    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private RelicMapper relicMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TattletaleAPI tattletaleAPI;

    /**
     * 用户待处理任务
     *
     * @param sourceId 机构id
     * @param userId   用户id
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageInfo alarmTasks(String sourceId, String userId, Integer pageNum, Integer pageSize) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getUserTaskUrl());
        String url = builder.queryParam("sourceId", sourceId).queryParam("userId", userId)
                .queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<PageInfo<AlarmRecord>>>() {
        };

        Result<PageInfo<AlarmRecord>> result = (Result<PageInfo<AlarmRecord>>) restTemplate.exchange(url, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        if (result.getCode() == TattletaleAPI.SUCCESS ) {
            PageInfo pageInfo = result.getData();
            assemRelic(pageInfo.getList());
            return pageInfo;
        } else {
            throw new Exception("查询报警记录失败");
        }
    }

    /**
     * 组装文物信息
     *
     * @param alarmRecords
     */
    public void assemRelic(List<AlarmRecord> alarmRecords) {
        for (AlarmRecord alarmHistory : alarmRecords) {
            Relic relic = relicMapper.selectByPrimaryKey(alarmHistory.getAlarmpointid());
            alarmHistory.setRelic(relic);
        }
    }

    /**
     * 通过id查询报警任务
     *
     * @param id
     * @return
     * @throws Exception
     */
    public AlarmRecord alarmTask(String id) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getEditTaskUrl());
        String url = builder.build().expand(id).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<AlarmRecord>>() {
        };

        Result<AlarmRecord> result = (Result<AlarmRecord>) restTemplate.exchange(url, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        if (result.getCode() == TattletaleAPI.SUCCESS) {
            AlarmRecord alarmRecord = result.getData();
            List<AlarmRecord> alarmRecords = Lists.newArrayList();
            alarmRecords.add(alarmRecord);
            assemRelic(alarmRecords);
            return alarmRecord;
        } else {
            throw new Exception("跳转任务处理页面失败");
        }
    }

    /**
     * 用户待处理任务数
     *
     * @param sourceId 机构id
     * @param userId   用户id
     * @return
     * @throws Exception
     */
    public int alarmTaskCount(String sourceId, String userId) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getUserPendingCountUrl());
        String url = builder.queryParam("sourceId", sourceId).queryParam("userId", userId).toUriString();
//        Result result = restTemplateUtil.find(url);
//        if (result.getCode() == TattletaleAPI.SUCCESS) {
//            return (int) ((Map) result.getData()).get("userPendingCount");
//        } else {
//            throw new Exception("查询用户待处理任务数失败");
//        }
        return 0;
    }

    /**
     * 处理报警任务
     *
     * @param alarmRecord
     * @return
     * @throws Exception
     */
    public void update(AlarmRecord alarmRecord) throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getUpdateTaskUrl());
        String url = builder.toUriString();
        String currentUser = UserUtils.getCurrentUser().getId();
        alarmRecord.setTransactor(currentUser);
        alarmRecord.setHandletime(new Date());
        alarmRecord.setState(1);
        Result result = restTemplateUtil.execute(alarmRecord, url);
        if (result.getCode() != TattletaleAPI.SUCCESS) {
            throw new Exception("处理报警任务失败");
        }
    }
}
