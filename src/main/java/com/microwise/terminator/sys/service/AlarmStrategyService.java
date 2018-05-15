package com.microwise.terminator.sys.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.microwise.terminator.sys.entity.*;
import com.microwise.terminator.sys.mapper.SensorinfoMapper;
import com.microwise.terminator.sys.mapper.UserMapper;
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
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 报警策略service
 *
 * @author bai.weixing
 * @since 2017/11/9.
 */
@Service
@Transactional
public class AlarmStrategyService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SensorinfoMapper sensorinfoMapper;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TattletaleAPI tattletaleAPI;


    /**
     * 根据条件查询报警策略
     *
     * @param alarmPointId
     * @return
     * @throws Exception
     */
    public List<AlarmStrategy> alarmStrategies(String alarmPointId) throws Exception {
        String sourceId = UserUtils.getCurrentUser().getOfficeId();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(tattletaleAPI.getAlarmStrategyUrl());
        String url = builder.build().expand(2, sourceId, 2, alarmPointId).toUriString();

        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<List<AlarmStrategy>>>() {
        };
        Result<List<AlarmStrategy>> result = (Result<List<AlarmStrategy>>)
                restTemplate.exchange(url, HttpMethod.GET, null, parameterizedTypeReference).getBody();

        if (TattletaleAPI.SUCCESS == result.getCode()) {
            List<AlarmStrategy> alarmStrategies = result.getData();
            assebleSensorData(alarmStrategies);
            return alarmStrategies;
        } else {
            throw new Exception("查询报警策略失败");
        }
    }

    private void assebleSensorData(List<AlarmStrategy> alarmStrategies) {
        for (AlarmStrategy alarmStrategy : alarmStrategies) {
            for (AlarmThreshold alarmThreshold : alarmStrategy.getAlarmthresholds()) {
                Example sensorExample = new Example(Sensorinfo.class);
                sensorExample.createCriteria().andEqualTo("sensorphysicalid", alarmThreshold.getSensorphysicalid());
                Sensorinfo sensorinfo = sensorinfoMapper.selectByExample(sensorExample).get(0);
                alarmThreshold.setCnName(sensorinfo.getCnName());
                alarmThreshold.setUnits(sensorinfo.getUnits());
            }
        }
    }

    /**
     * 根据id查询报警策略
     *
     * @param id
     * @return
     */
    public AlarmStrategy getAlarmStrategy(String id) throws Exception {
        String url = UriComponentsBuilder.fromUriString(tattletaleAPI.getEditAlarmStrategyUrl()).
                build().expand(id).toUriString();
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<Result<AlarmStrategy>>() {
        };
        Result<AlarmStrategy> result = (Result<AlarmStrategy>)
                restTemplate.exchange(url, HttpMethod.GET, null, parameterizedTypeReference).getBody();
        if (result.getCode() == TattletaleAPI.SUCCESS) {
            AlarmStrategy alarmStrategy = result.getData();
            List<AlarmStrategy> alarmStrategies = Lists.newArrayList();
            alarmStrategies.add(alarmStrategy);
            assebleSensorData(alarmStrategies);
            return alarmStrategy;
        } else {
            throw new Exception("跳转修改页面失败");
        }
    }

    /**
     * 保存报警策略
     *
     * @param alarmStrategy
     * @return
     */
    public void save(AlarmStrategy alarmStrategy) throws Exception {
        alarmStrategy.setSystemid("2");
        alarmStrategy.setSourceid(UserUtils.getCurrentUser().getOfficeId());
        alarmStrategy.setAlarmpointtype(2);
        List<Notifier> notifiers = alarmStrategy.getNotifiers();
        List<Notifier> notifierList = Lists.newArrayList();
        assemNotifier(notifiers, notifierList);
        Gson gson = new Gson();
        alarmStrategy.setNotifier(gson.toJson(notifierList));
        String url = UriComponentsBuilder.fromUriString(tattletaleAPI.getSaveAlarmStrategyUrl()).toUriString();
        Result result = restTemplateUtil.execute(alarmStrategy, url);
        if (result.getCode() != TattletaleAPI.SUCCESS) {
            throw new Exception("添加报警策略失败");
        }
    }

    /**
     * 更新报警策略
     *
     * @param alarmStrategy
     * @return
     */
    public void update(AlarmStrategy alarmStrategy) throws Exception {
        List<Notifier> notifiers = alarmStrategy.getNotifiers();
        List<Notifier> notifierList = Lists.newArrayList();
        assemNotifier(notifiers, notifierList);
        Gson gson = new Gson();
        alarmStrategy.setNotifier(gson.toJson(notifierList));
        String url = UriComponentsBuilder.fromUriString(tattletaleAPI.getUpdateAlarmStrategyUrl()).toUriString();
        Result result = restTemplateUtil.execute(alarmStrategy, url);
        if (result.getCode() != TattletaleAPI.SUCCESS) {
            throw new Exception("更新报警策略失败");
        }
    }

    private void assemNotifier(List<Notifier> notifiers, List<Notifier> notifierList) {
        for (Notifier notifier : notifiers) {
            User user = userMapper.selectByPrimaryKey(notifier.getId());
            notifier.setMobile(user.getMobile());
            notifier.setEmail(user.getEmail());
            notifier.setName(user.getName());
            notifierList.add(notifier);
        }
    }

    /**
     * 删除报警策略
     *
     * @param id
     * @return
     */
    public void delete(String id) throws Exception {
        String url = UriComponentsBuilder.fromUriString(tattletaleAPI.getDeleteAlarmStrategyUrl())
                .build().expand(id).toUriString();
        Result result = restTemplateUtil.delete(url);
        if (result.getCode() != TattletaleAPI.SUCCESS) {
            throw new Exception("删除报警策略失败");
        }
    }


}
