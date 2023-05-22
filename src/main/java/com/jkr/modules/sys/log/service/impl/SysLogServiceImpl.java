package com.jkr.modules.sys.log.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jkr.core.base.service.impl.BaseServiceImpl;
import com.jkr.modules.sys.log.mapper.SysLogMapper;
import com.jkr.modules.sys.log.model.SysLog;
import com.jkr.modules.sys.log.service.ISysLogService;
import org.springframework.stereotype.Service;

/**
 * @author zenglingwen
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLog> implements ISysLogService {


    /**
     * @title: findLogPage
     * @author: wanghe
     * @date: 2022/5/8 17:01
     * @description: 日志列表查询
     * @param page
     * @param sysLog
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.jkr.modules.sys.log.model.SysLog>
     */
    @Override
    public IPage<SysLog> findLogPage(IPage<SysLog> page, SysLog sysLog) {
        return baseMapper.findLogPage(page, sysLog);
    }
}
