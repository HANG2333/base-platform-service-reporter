package com.jkr.modules.sys.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jkr.common.annotation.WebLog;
import com.jkr.common.model.ResponseData;
import com.jkr.core.base.controller.BaseController;
import com.jkr.modules.sys.log.model.SysLog;
import com.jkr.modules.sys.log.service.ISysLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zenglingwen
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController extends BaseController<ISysLogService, SysLog> {

    /**
     * @title: findLogPage
     * @author: wanghe
     * @date: 2022/5/8 16:59
     * @description: 日志列表分页
     * @param sysLog
     * @return: com.jkr.common.model.ResponseData<com.baomidou.mybatisplus.core.metadata.IPage<com.jkr.modules.sys.log.model.SysLog>>
     */
    @WebLog(description = "日志列表分页")
    @GetMapping("/findLogPage")
    public ResponseData<IPage<SysLog>> findLogPage(SysLog sysLog) {
        IPage<SysLog> page = new Page<SysLog>(sysLog.getCurrent(), sysLog.getSize());
        return ResponseData.success(baseService.findLogPage(page,sysLog));
    }
}
