package com.microwise.terminator.sys.interceptor;

import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.service.AlarmTaskService;
import com.microwise.terminator.sys.util.Constants;
import com.microwise.terminator.sys.util.UserUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/12/22.
 */
@Aspect
@Component
public class MyAlarmTaskInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(MyAlarmTaskInterceptor.class);

    @Autowired
    private AlarmTaskService alarmTaskService;

    @Pointcut("execution(* com.microwise.terminator.sys.service..*(..)) " +
            "&& !execution(* Object.*(..))" +
            "&& !execution(* com.microwise.terminator.sys.service.AlarmTaskService.alarmTaskCount(..))")
    public void myAlarmTaskMethodPointcut() {
    }

    @Before("myAlarmTaskMethodPointcut()")
    public void Interceptor() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession session = attributes.getRequest().getSession();
        Integer alarmTaskCount = (Integer) session.getAttribute(Constants.Session.ALARM_TASK_COUNT);
        //更新session中我的报警任务
        if (alarmTaskCount != null) {
            User user = UserUtils.getCurrentUser();
            String notifier = user.getId();
            String officeId = user.getOfficeId();
            try {
                alarmTaskCount = alarmTaskService.alarmTaskCount(officeId, notifier);
            } catch (Exception e) {
                alarmTaskCount = 0;
                logger.error("查询报警任务失败", e);
            }
            session.removeAttribute(Constants.Session.ALARM_TASK_COUNT);
            session.setAttribute(Constants.Session.ALARM_TASK_COUNT, alarmTaskCount);
        }
    }
}
