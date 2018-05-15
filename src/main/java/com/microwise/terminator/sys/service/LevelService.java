package com.microwise.terminator.sys.service;

import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.sys.entity.Level;
import com.microwise.terminator.sys.mapper.LevelMapper;
import org.springframework.stereotype.Service;

/**
 * 级别service
 *
 * @author bai.weixing
 * @since 2017/9/21.
 */
@Service
public class LevelService extends CrudService<LevelMapper, Level> {
}
