package com.jkr.modules.sys.menu.service;

import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.dict.model.SysDict;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuDTO;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
public interface ISysMenuService extends BaseService<SysMenu> {


    /**
     * method_name: getMenuTreeTableList
     * create_user: jikeruan
     * create_date: 2020/9/3
     * create_time: 16:16
     * describe: 获取菜单树列表
     * param : []
     * return : com.jkr.common.model.ResponseData<java.util.List<com.jkr.modules.menu.model.SysMenu>>
     */
    List<SysMenu> getMenuTreeTableList();

    /**
     * method_name: getMenuTreeListByUserId
     * create_user: sunhao
     * create_date: 2020/8/10
     * create_time: 10:04
     * describe: 根据用户获取菜单
     * param : [userId]
     * return : java.util.Set<java.lang.String>
     */
    List<SysMenu> getMenuTreeListByUserId(String userId);

    /**
     * @title: [menuDTO]
     * @author: lzy
     * @date: 2022/4/12 19:56
     * @description: 菜单列表查询
     * @param: [menuDTO]
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.menu.model.SysMenu>>
     */
    List<SysMenu> menuList(MenuDTO menuDTO);

    @Override
    boolean saveOrUpdate(SysMenu sysMenu);

}
