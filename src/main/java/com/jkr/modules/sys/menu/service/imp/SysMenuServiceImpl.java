package com.jkr.modules.sys.menu.service.imp;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jkr.common.constant.CommonConstant;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.dict.model.SysDict;
import com.jkr.modules.sys.menu.mapper.SysMenuMapper;
import com.jkr.modules.sys.menu.model.SysMenu;
import com.jkr.modules.sys.menu.model.dto.MenuDTO;
import com.jkr.modules.sys.menu.service.ISysMenuService;
import com.jkr.modules.sys.user.service.ISysUserService;
import com.jkr.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author jikeruan
 * @since 2019-09-06
 */
@Service
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysUserService sysUserInfoService;


    /**
     * method_name: getMenuTreeTableList
     * create_user: jikeruan
     * create_date: 2020/9/3
     * create_time: 16:16
     * describe: 获取菜单树列表
     * param : []
     * return : com.jkr.common.model.ResponseData<java.util.List<com.jkr.modules.menu.model.SysMenu>>
     */
    @Override
    public List<SysMenu> getMenuTreeTableList() {
        List<SysMenu> sysMenus = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>().orderByAsc(SysMenu::getLocation));
        List<SysMenu> treeDataList = new ArrayList<>();
        sysMenus.forEach(sysMenu -> {
            if (CommonConstant.DEFAULT_PARENT_VAL.equalsIgnoreCase(sysMenu.getParentId())) {
                treeDataList.add(sysMenu);
            }
        });
        recursionTreeTableChildren(treeDataList, sysMenus);
        return treeDataList;
    }

    /**
     * method_name: getMenuTreeListByUserId
     * create_user: jikeruan
     * create_date: 2020/9/3
     * create_time: 16:18
     * describe:  根据用户获取菜单
     * param : [userId]
     * return : java.util.List<com.jkr.modules.menu.model.SysMenu>
     */
    @Override
    public List<SysMenu> getMenuTreeListByUserId(String userId) {
        List<SysMenu> allMenu = sysUserInfoService.getAllMenuByUserId(userId);
        List<SysMenu> treeData = baseMapper.selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getType, CommonConstant.ONE_NUMBER)
                .ne(SysMenu::getParentId, CommonConstant.ZERO_NUMBER)
                .orderByAsc(SysMenu::getCreateDate));
        recursionTreeTableChildren(treeData, allMenu);
        return treeData.stream().filter(data -> null != data.getChildren() && data.getChildren().size() > 0).collect(Collectors.toList());
    }


    /**
     * method_name: recursionTreeTableChildren
     * create_user: jikeruan
     * create_date: 2020/9/3
     * create_time: 16:16
     * describe: 递归菜单树
     * param : [treeDataList, sysModuleResources]
     * return : void
     */
    private void recursionTreeTableChildren(List<SysMenu> treeDataList, List<SysMenu> sysModuleResources) {
        for (SysMenu treeData : treeDataList) {
            List<SysMenu> childrenList = new ArrayList<>();
            for (SysMenu sysMenu : sysModuleResources) {
                if (sysMenu.getParentId().equals(treeData.getId())) {
                    childrenList.add(sysMenu);
                }
            }
            if (!CollUtil.isEmpty(childrenList)) {
                treeData.setChildren(childrenList);
                recursionTreeTableChildren(childrenList, sysModuleResources);
            }
        }
    }

    /**
     * @title: [menuDTO]
     * @author: lzy
     * @date: 2022/4/12 19:56
     * @description: 菜单列表查询
     * @param: [menuDTO]
     * @return: com.jkr.common.model.ResponseData<java.util.List < com.jkr.modules.sys.menu.model.SysMenu>>
     */
    @Override
    public List<SysMenu> menuList(MenuDTO menuDTO) {
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        if (null != menuDTO.getTypeBegin()) {
            queryWrapper.gt("type", menuDTO.getTypeBegin());
        }
        if (null != menuDTO.getTypeEnd()) {
            queryWrapper.lt("type", menuDTO.getTypeEnd());
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(SysMenu sysMenu) {
        // 获取parentId，用于新增或更新时的parentIds赋值
        String parentId = sysMenu.getParentId();
        String parentIds = "0";
        QueryWrapper<SysMenu> queryWrapper = new QueryWrapper<>();
        // QueryWrapper如果select的字段为null，则整个查询结果为null
        queryWrapper.select("id", "parent_ids").eq("id", parentId);
        SysMenu parentMenu = super.getOne(queryWrapper);
        if (null != parentMenu) {
            parentIds = parentMenu.getParentIds() + "," + sysMenu.getParentId();
        }
        sysMenu.setParentId(parentId);
        sysMenu.setParentIds(parentIds);
        return super.saveOrUpdate(sysMenu);
    }
}
