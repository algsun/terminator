package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.sys.entity.Role;
import com.microwise.terminator.sys.entity.UserRole;
import com.microwise.terminator.sys.mapper.RoleMapper;
import com.microwise.terminator.sys.mapper.UserRoleMapper;
import com.microwise.terminator.sys.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 角色service
 *
 * @author sun.cong
 * @since 2017/8/30
 */
@Service
@Transactional
public class RoleService extends CrudService<RoleMapper, Role> {

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 查找所有未被删除的角色
     *
     * @return
     */
    public List<Role> findByExample() {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("delFlag", 0);
        return mapper.selectByExample(example);
    }

    /**
     * 查询所有角色不分页
     *
     * @return
     */
    public List<Role> findAll() {
        return UserUtils.getRoles();
    }

    /**
     * 查询角色带分页
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public PageInfo findAllPagination(int pageNumber, int pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("delFlag", "0");
        List<Role> roles = mapper.selectByExample(example);
        PageInfo<Role> pageInfo = new PageInfo(roles);
        return pageInfo;
    }

    /**
     * 根据名称查询角色
     *
     * @param name
     * @return
     */
    public List<Role> findByName(String name) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("name", name).andEqualTo("delFlag", "0");
        return mapper.selectByExample(example);
    }

    /**
     * 保存或更新角色
     *
     * @param role
     */
    public void saveOrupdate(Role role) {
        if (StringUtils.isBlank(role.getId())) {
            role.preInsert();
            mapper.insertSelective(role);
        } else {
            role.preUpdate();
            mapper.updateByPrimaryKeySelective(role);
        }
        //维护角色和菜单中间表
        mapper.deleteRoleMenu(role);
        if (role.getMenus().size() > 0) {
            mapper.insertRoleMenu(role);
        }
        // 清除用户角色缓存
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
    }

    /**
     * 删除角色
     *
     * @param id 角色id
     */
    public void delete(String id) {
        Role role = mapper.selectByPrimaryKey(id);
        role.setDelFlag(true);
        mapper.updateByPrimaryKeySelective(role);
        mapper.deleteRoleMenu(role);
        UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
    }

    /**
     * 查询角色及每个角色所拥有的权限
     *
     * @param id
     * @return
     */
    public Role find(String id) {
        return mapper.find(id);
    }

    /**
     * 验证角色是否被使用
     *
     * @param id 角色id
     * @return
     */
    public boolean isUsed(String id) {
        boolean used = false;
        UserRole userRole = new UserRole();
        userRole.setRoleId(id);
        List<UserRole> userRoles = userRoleMapper.select(userRole);
        if (userRoles != null && userRoles.size() > 0) {
            used = true;
        }
        return used;
    }

}
