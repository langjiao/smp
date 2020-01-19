package com.xuaiyuan.smp.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuaiyuan.smp.modules.sys.entity.SysLoginLogEntity;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface SysLoginLogDao extends BaseMapper<SysLoginLogEntity> {
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
