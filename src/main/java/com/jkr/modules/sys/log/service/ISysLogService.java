package com.jkr.modules.sys.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.core.base.service.BaseService;
import com.jkr.modules.sys.log.model.SysLog;
import org.apache.ibatis.annotations.Param;

/**
 * @author zenglingwen
 */
public interface ISysLogService extends BaseService<SysLog> {

    /**
     * @title: findLogPage
     * @author: wanghe
     * @date: 2022/5/8 17:00
     * @description: 日志列表分页
     * @param page
     * @param sysLog
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jkr.modules.sys.log.model.SysLog>
     */
    IPage<SysLog> findLogPage(@Param("page") IPage<SysLog> page, @Param("sysLog") SysLog sysLog);
}
