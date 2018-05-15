package com.microwise.terminator.sys.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by lijianfei on 2017/12/6.
 *
 * @author li.jianfei
 * @since 2017/12/6
 */
@Component
public class TattletaleAPI {
    @Value("${tattletaleURL}")
    private String tattletaleUrl;
    /**
     * API调用成功
     */
    public static final int SUCCESS = 200;

    /**
     * 报警记录
     *
     * @return
     */
    public String getAlarmRecordUrl() {
        return tattletaleUrl + "alarm-records";
    }

    /**
     * 报警点报警记录
     *
     * @return
     */
    public String getAlarmPointRecordUrl() {
        return tattletaleUrl + "alarm-records/alarm-point";
    }

    /**
     * 统计日报
     *
     * @return
     */
    public String getDailyReportUrl() {
        return tattletaleUrl + "alarm-records/daily-report";
    }

    /**
     * 统计日报中的报警记录
     *
     * @return
     */
    public String getReportAlarm() {
        return tattletaleUrl + "alarm-records/daily-report/alarm-history";
    }

    /**
     * 获取用户任务列表url
     *
     * @return
     */
    public String getUserTaskUrl() {
        return tattletaleUrl + "alarm-records/user-task";
    }

    /**
     * 获取编辑用户任务url
     *
     * @return
     */
    public String getEditTaskUrl() {
        return tattletaleUrl + "alarm-records/{id}";
    }

    /**
     * 获取用户待处理任务总数url
     *
     * @return
     */
    public String getUserPendingCountUrl() {
        return tattletaleUrl + "alarm-records/user-pending-count";
    }

    /**
     * 获取处理用户任务的url
     *
     * @return
     */
    public String getUpdateTaskUrl() {
        return tattletaleUrl + "alarm-records/update";
    }

    /**
     * 获取报警策略列表url
     *
     * @return
     */
    public String getAlarmStrategyUrl() {
        return tattletaleUrl + "alarmStrategies/{systemId}/{sourceId}/{alarmPointType}/{alarmPointId}";
    }

    /**
     * 获取编辑报警策略url
     *
     * @return
     */
    public String getEditAlarmStrategyUrl() {
        return tattletaleUrl + "alarmStrategies/{id}";
    }

    /**
     * 获取报警策略添加url
     *
     * @return
     */
    public String getSaveAlarmStrategyUrl() {
        return tattletaleUrl + "alarmStrategies";
    }

    /**
     * 获取报警策略修改url
     *
     * @return
     */
    public String getUpdateAlarmStrategyUrl() {
        return tattletaleUrl + "alarmStrategies/update";
    }

    /**
     * 获取报警策略删除url
     *
     * @return
     */
    public String getDeleteAlarmStrategyUrl() {
        return tattletaleUrl + "alarmStrategies/{id}";
    }

    /**
     * 获取知晓记录url
     *
     * @return
     */
    public String getAwareRecordsUrl() {
        return tattletaleUrl + "aware/record";
    }

    /**
     * 保存知晓记录url
     *
     * @return
     */
    public String saveAwareRecordList() {
        return tattletaleUrl + "aware/record/addAll";
    }

    /**
     * 获取知晓记录url(模糊查询)
     *
     * @return
     */
    public String getAwareRecordsBlurryUrl() {
        return tattletaleUrl + "/aware/record/aware-record";
    }
}
