package com.microwise.terminator.common.persistence;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 公共Mapper, 业务Mapper继承该Mapper
 *
 * @author lijianfei
 * @since 2017-03-02
 */
public interface TerminatorMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //FIXME 特别注意，该接口不能被扫描到，否则会出错
}
