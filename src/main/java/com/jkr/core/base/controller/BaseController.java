package com.jkr.core.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.service.BaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author：jikeruan
 * @Description: 公共controller
 * @Date: 2019/9/6 13:19
 */
@Slf4j
public class BaseController<Service extends BaseService<Entity>, Entity> {

    /**
     * 注入的service
     */
    @Autowired
    protected Service baseService;


    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "entity", value = "查询对象", dataType = "Entity", paramType = "query")
    })
    @GetMapping(value = "/page")
    public ResponseData<IPage<Entity>> page(Entity entity) {
        return ResponseData.success(baseService.pageList(entity));
    }

    @ApiOperation(value = "获取全部信息", notes = "获取全部信息")
    @GetMapping(value = "/all")
    public ResponseData<List<Entity>> all() {
        return ResponseData.success(baseService.list());
    }


    @ApiOperation(value = "根据对象id，查询详细信息", notes = "根据对象id，查询详细信息")
    @GetMapping(value = "/getDetailById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "实体类Id", dataType = "Serializable", paramType = "query")
    })
    public ResponseData<Entity> getEntityById(@RequestParam Serializable id) {
        return ResponseData.success(baseService.getById(id));
    }

    @ApiOperation(value = "添加或者修改", notes = "添加或者修改，先根据id查询是否能查到，无就添加，有就修改")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Entity", name = "entity", value = "对象参数", required = true)
    })
    @PostMapping(value = "/saveOrUpdate")
    public ResponseData<Boolean> saveOrUpdate(@RequestBody Entity entity) {
        return ResponseData.success(baseService.saveOrUpdate(entity));
    }

    @ApiOperation(value = "添加", notes = "添加")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Entity", name = "entity", value = "对象参数", required = true)
    })
    @PostMapping(value = "/save")
    public ResponseData<Boolean> save(@RequestBody Entity entity) {
        return ResponseData.success(baseService.save(entity));
    }

    @ApiOperation(value = "修改", notes = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "Entity", name = "entity", value = "对象参数", required = true)
    })
    @PostMapping(value = "/update")
    public ResponseData<Boolean> update(@RequestBody Entity entity) {
        return ResponseData.success(baseService.updateById(entity));
    }

    @ApiOperation(value = "删除", notes = "删除")
    @PostMapping(value = "/delete")
    public ResponseData<Boolean> delete(@RequestParam Serializable id) {
        return ResponseData.success(baseService.removeById(id));
    }

    @ApiOperation(value = "根据list删除", notes = "根据list删除")
    @PostMapping(value = "/deleteBatch")
    public ResponseData<Boolean> deleteBatch(@RequestBody Collection<? extends Serializable> idList) {
        return ResponseData.success(baseService.removeByIds(idList));
    }
}
