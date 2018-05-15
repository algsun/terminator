package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.sys.entity.Office;
import com.microwise.terminator.sys.mapper.OfficeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 机构service
 *
 * @author sun.cong
 * @since 2017/8/25
 */
@Service
@Transactional
public class OfficeService extends CrudService<OfficeMapper, Office> {

    /**
     * 分页查找所有机构
     *
     * @return
     */
    public PageInfo<Office> findOffices(boolean deleted, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<Office> pageInfo = new PageInfo(this.findOffices(deleted));
        return pageInfo;
    }

    /**
     * 查找机构
     *
     * @return
     */
    public Office findOffice(String officeName, Integer id) {
        Example example = new Example(Office.class);
        example.createCriteria().andNotEqualTo("id", id);
        example.and().andEqualTo("delFlag", false);
        example.and().andEqualTo("officeName", officeName);
        List<Office> offices = mapper.selectByExample(example);
        if (offices.isEmpty()) return null;
        return offices.get(0);
    }

    /**
     * 查找机构
     *
     * @return
     */
    public Office findOffice(Integer id) {
        Example example = new Example(Office.class);
        example.createCriteria().andEqualTo("id", id);
        example.and().andEqualTo("delFlag", false);
        List<Office> offices = mapper.selectByExample(example);
        if (offices.isEmpty()) return null;
        return offices.get(0);
    }

    /**
     * 判断机构代码是否存在
     *
     * @return
     */
    public Office idExists(Integer id) {
        Example example = new Example(Office.class);
        example.createCriteria().andEqualTo("id", id);
        List<Office> offices = mapper.selectByExample(example);
        if (offices.isEmpty()) return null;
        return offices.get(0);
    }

    /**
     * 查找所有机构
     *
     * @param deleted
     * @return
     */
    public List<Office> findOffices(boolean deleted) {
        Example example = new Example(Office.class);
        example.createCriteria().andEqualTo("delFlag", deleted);
        example.setOrderByClause("`create_date` DESC");
        return mapper.selectByExample(example);
    }

    /**
     * 机构伪删除
     *
     * @param id
     */
    public void delete(Integer id) {
        Office office = this.findOffice(id);
        office.setDelFlag(true);
        super.update(office);
    }
}
