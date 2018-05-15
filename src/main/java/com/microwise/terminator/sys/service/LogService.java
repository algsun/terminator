package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.Log;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.mapper.LogMapper;
import com.microwise.terminator.sys.mapper.OfficeMapper;
import com.microwise.terminator.sys.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijianfei on 2017/9/5.
 *
 * @author li.jianfei
 * @since 2017/9/5
 */
@Service
@Transactional
public class LogService extends CrudService<LogMapper, Log> {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OfficeMapper officeMapper;

    /**
     * 查找日志
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<Log> findLogs(List<Log> logs, int pageNum, int pageSize) {
        Example example = new Example(Log.class);
        example.createCriteria().andBetween("createDate", logs.get(0).getBeginDate(), logs.get(0).getEndDate());
        if (StringUtils.isNotBlank(logs.get(0).getTitle())) {
            example.and().andLike("title", "%" + logs.get(0).getTitle() + "%");
        }
        Example.Criteria criteria = example.and();
        for (Log log : logs) {
            if (StringUtils.isNotBlank(log.getCreateBy())) {
                criteria = criteria.orEqualTo("createBy", log.getCreateBy());
            }
        }
        if (StringUtils.isNotBlank(logs.get(0).getRequestUri())) {
            example.and().andLike("requestUri", "%" + logs.get(0).getRequestUri() + "%");
        }
        example.setOrderByClause("`create_date` DESC");
        PageHelper.startPage(pageNum, pageSize);
        List<Log> logList = mapper.selectByExample(example);
        for (Log log : logList) {
            User user = userMapper.selectByPrimaryKey(log.getCreateBy());
            if (user != null) {
                log.setCreateByName(user.getName());
                Office office = officeMapper.selectByPrimaryKey(Integer.parseInt(user.getOfficeId()));
                if (office != null) {
                    log.setOfficeName(office.getOfficeName());
                }
            }
        }
        return new PageInfo(logList);
    }
}
