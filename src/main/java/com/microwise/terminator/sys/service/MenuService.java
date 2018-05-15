package com.microwise.terminator.sys.service;

import com.google.common.collect.Lists;
import com.microwise.terminator.common.service.CrudService;
import com.microwise.terminator.sys.entity.Menu;
import com.microwise.terminator.sys.mapper.MenuMapper;
import com.microwise.terminator.sys.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 菜单service
 *
 * @author bai.weixing
 * @since 2017/8/28.
 */
@Service
@Transactional
public class MenuService extends CrudService<MenuMapper, Menu>{

    public List<Menu> findAll() {
        List<Menu> userMenus = UserUtils.getMenus();
        List<Menu> menus = Lists.newArrayList();
        Menu.sortList(menus, userMenus, Menu.getRootId(), true);
        return menus;
    }

    public void updateMenuSort(Menu menu) {
        super.update(menu);
        // 清除用户菜单缓存
        UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
    }
}
