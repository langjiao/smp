package com.xuaiyuan.smp.modules.sys.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuaiyuan.smp.common.utils.PageUtils;
import com.xuaiyuan.smp.modules.sys.entity.SysLoginLogEntity;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

public interface SysLoginLogService extends IService<SysLoginLogEntity> {
    /**
     * 获取登录日志分页信息
     *
     */
    PageUtils findLoginLogs(Map<String, Object> params);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(SysLoginLogEntity loginLog);

    /**
     * 删除登录日志
     *
     * @param ids 日志 id集合
     */
    void deleteLoginLogs(String[] ids);

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近七天来的访问记录
     *
     * @param sysUserEntity 用户
     * @return 系统近七天来的访问记录
     */
    List<Map<String, Object>> findLastSevenDaysVisitCount(SysUserEntity sysUserEntity);
}
