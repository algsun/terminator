package com.microwise.terminator.sys.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.microwise.terminator.common.security.Digests;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.common.utils.Encodes;
import com.microwise.terminator.common.utils.StringUtils;
import com.microwise.terminator.common.web.Servlets;
import com.microwise.terminator.sys.entity.User;
import com.microwise.terminator.sys.entity.UserRole;
import com.microwise.terminator.sys.mapper.OfficeMapper;
import com.microwise.terminator.sys.mapper.UserMapper;
import com.microwise.terminator.sys.mapper.UserRoleMapper;
import com.microwise.terminator.sys.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by lijianfei on 2017/8/25.
 *
 * @author li.jianfei
 * @since 2017/8/25
 */
@Service
@Transactional
public class UserService extends CrudService<UserMapper, User> {


    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 根据登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    public User getUserByLoginName(String loginName) {
        return UserUtils.getByLoginName(loginName);
    }

    @Transactional(readOnly = false)
    public void updateUserLoginInfo(User user) { // 保存上次登录信息
        user.setOldLoginIp(user.getLoginIp());
        user.setOldLoginDate(user.getLoginDate());
        // 更新本次登录信息
        user.setLoginIp(StringUtils.getRemoteAddr(Servlets.getRequest()));
        user.setLoginDate(new Date());
        mapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 查询所有未被删除的用户
     *
     * @return
     */
    public List<User> findUsers() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("delFlag", false);
        example.setOrderByClause("`create_date` DESC");
        return mapper.selectByExample(example);
    }

    /**
     * 查询关注/未关注的用户
     *
     * @return
     */
    public List<User> findUsersByAttention(Integer value) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("attention", value);
        example.setOrderByClause("`create_date` DESC");
        return mapper.selectByExample(example);
    }

    /**
     * 分页查询所有未被删除的用户
     *
     * @return
     */
    public PageInfo<User> findUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = this.findUsers();
        for (User user : users) {
            user.setOffice(officeMapper.selectByPrimaryKey(Integer.parseInt(user.getOfficeId())));
        }
        PageInfo<User> pageInfo = new PageInfo(users);
        return pageInfo;
    }

    /**
     * 查询用户
     *
     * @param condition
     * @return
     */
    public User findUser(String condition, String id) {
        List<User> users = mapper.findUser(condition, id);
        if (users.isEmpty()) return null;
        return users.get(0);
    }

    /**
     * 通过用户姓名查找用户（不是登录名）
     *
     * @param name
     * @return
     */
    public List<User> findUsersByName(String name) {
        Example example = new Example(User.class);
        example.createCriteria().andLike("name", "%" + name + "%");
        return mapper.selectByExample(example);
    }

    /**
     * 增加用户（同时增加用户角色关联数据）
     *
     * @param user
     */
    public void save(User user) {
        user.preInsert();
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(this.entryptPassword(user.getPassword()));
        }
        if (StringUtils.isBlank(user.getOfficeId())) {
            user.setOfficeId("");
        }
        mapper.insertSelective(user);
        if (user.getRolesId() != null) {
            for (String id : user.getRolesId()) {
                userRoleMapper.insertSelective(new UserRole(user.getId(), id));
            }
        }
    }

    /**
     * 查找被选中的角色
     *
     * @param id
     * @return
     */
    public User findUserWithRoles(String id) {
        User user = mapper.selectByPrimaryKey(id);
        Example example = new Example(UserRole.class);
        example.createCriteria().andEqualTo("userId", id);
        List<UserRole> userRoles = userRoleMapper.selectByExample(example);
        String[] rolesId = new String[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            rolesId[i] = userRoles.get(i).getRoleId();
        }
        if (rolesId != null) {
            user.setRolesId(rolesId);
        }
        return user;
    }

    /**
     * 修改用户
     *
     * @param user
     */
    public void updateUserWithRole(User user) {
        user.preUpdate();
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(this.entryptPassword(user.getPassword()));
        } else {
            user.setPassword(mapper.selectByPrimaryKey(user.getId()).getPassword());
        }
        mapper.updateByPrimaryKeySelective(user);
        userRoleMapper.delete(new UserRole(user.getId()));
        if (user.getRolesId() != null) {
            for (String id : user.getRolesId()) {
                userRoleMapper.insertSelective(new UserRole(user.getId(), id));
            }
        }
    }

    /**
     * 伪删除用户
     *
     * @param id
     */
    public void delete(String id) {
        User user = mapper.selectByPrimaryKey(id);
        user.setDelFlag(true);
        mapper.updateByPrimaryKeySelective(user);
        userRoleMapper.delete(new UserRole(id));
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public String entryptPassword(String plainPassword) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
    }

}
