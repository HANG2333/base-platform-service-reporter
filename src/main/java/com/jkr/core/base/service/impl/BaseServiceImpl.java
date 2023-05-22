package com.jkr.core.base.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jkr.core.binding.QueryBuilder;
import com.jkr.core.base.model.PageModel;
import com.jkr.core.base.service.BaseService;

import java.util.List;

/**
 * @Author：jikeruan
 * @Description: 公共serviceImpl
 * @Date: 2019/9/6 11:09
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseService<T> {

    private static final String ORDER_ASC = "asc";
    private static final String ORDER_DESC = "desc";

    @Override
    public IPage<T> pageList(T entity) {
        return super.baseMapper.selectPage(getPagePlus(entity), Wrappers.query(entity));
    }

    @Override
    public <T1, DTO> IPage<T1> pageListByDto(DTO dto) {
        QueryWrapper<T> wrapper = QueryBuilder.toWrapper(dto);
        return (IPage<T1>) super.baseMapper.selectPage(getPagePlus(dto), wrapper);
    }

    @Override
    public <T1, DTO> IPage<T1> page(DTO dto, Wrapper<T1> queryWrapper) {
        return (IPage<T1>) super.baseMapper.selectPage(getPagePlus(dto), (Wrapper<T>) queryWrapper);
    }


    /**
     * 获取分页参数
     *
     * @param entity
     * @return
     */
    public IPage<T> getPagePlus(Object entity) {
        PageModel pageModel = (PageModel) entity;
        Page<T> pagePlus = new Page<T>();
        pagePlus.setCurrent(pageModel.getCurrent());
        pagePlus.setSize(pageModel.getSize());
        if (StrUtil.isNotBlank(pageModel.getOrder()) && StrUtil.isNotBlank(pageModel.getSort())) {
            List<OrderItem> orderItems = CollUtil.newArrayList();
            OrderItem orderItem = new OrderItem();
            // 驼峰式转换下划线方式
            orderItem.setColumn(StrUtil.toUnderlineCase(pageModel.getSort()));
            orderItem.setAsc(ORDER_ASC.equalsIgnoreCase(pageModel.getOrder()));
            orderItems.add(orderItem);
            // 添加排序字段
            pagePlus.addOrder(orderItems);
        }
        return pagePlus;
    }

}
