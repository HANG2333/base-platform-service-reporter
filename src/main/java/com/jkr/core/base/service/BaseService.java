package com.jkr.core.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @Author：jikeruan
 * @Description: 公共service
 * @Date: 2019/9/6 11:07
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 分页
     *
     * @param entity 实体类
     * @return
     */
    IPage<T> pageList(T entity);


    /**
     * 分页查询 根据dto查询
     *
     * @param dto
     * @param <T>
     * @param <DTO> dto对象
     * @return
     */
    <T, DTO> IPage<T> pageListByDto(DTO dto);


    /**
     * 分页查询 根据dto查询
     * @param dto
     * @param queryWrapper
     * @param <T>
     * @param <DTO>
     * @return
     */
    <T, DTO> IPage<T> page(DTO dto, Wrapper<T> queryWrapper);
}
