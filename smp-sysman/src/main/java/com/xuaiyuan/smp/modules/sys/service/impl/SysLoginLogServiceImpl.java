package com.xuaiyuan.smp.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuaiyuan.smp.common.utils.*;
import com.xuaiyuan.smp.modules.sys.dao.SysLoginLogDao;
import com.xuaiyuan.smp.modules.sys.entity.SysLoginLogEntity;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import com.xuaiyuan.smp.modules.sys.service.SysLoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author lj
 * @Date 2020/1/14 14:38
 */
@Service(value = "sysLoginLogService")
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogDao,SysLoginLogEntity> implements SysLoginLogService {
    @Override
    public PageUtils findLoginLogs(Map<String, Object> params) {
        QueryWrapper<SysLoginLogEntity> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(params.get("loginUser") == null?"":params.get("loginUser").toString())) {
            queryWrapper.eq("username",params.get("loginUser").toString().toLowerCase());
        }
        if (StringUtils.isNotBlank(params.get("loginTime") == null?"":params.get("loginTime").toString())) {
            queryWrapper.ge("login_time", params.get("startLoginTime").toString().toLowerCase()).le("login_time", params.get("endloginTime").toString().toLowerCase());
        }
        IPage<SysLoginLogEntity> page = page(new Query<SysLoginLogEntity>().getPage(params),queryWrapper);
        return new PageUtils(page);
    }
    @Override
    @Transactional
    public void saveLoginLog(SysLoginLogEntity sysLoginLogEntity) {
        sysLoginLogEntity.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        sysLoginLogEntity.setIp(ip);
        sysLoginLogEntity.setLocation(AddressUtil.getAddress(ip));
        this.save(sysLoginLogEntity);
    }
    @Override
    @Transactional
    public void deleteLoginLogs(String[] ids) {
        List<String> list = Arrays.asList(ids);
        baseMapper.deleteBatchIds(list);
    }
    @Override
    public Long findTotalVisitCount() {
        return this.baseMapper.findTotalVisitCount();
    }

    @Override
    public Long findTodayVisitCount() {
        return this.baseMapper.findTodayVisitCount();
    }

    @Override
    public Long findTodayIp() {
        return this.baseMapper.findTodayIp();
    }
    @Override
    public List<Map<String, Object>> findLastSevenDaysVisitCount(SysUserEntity sysUserEntity) {
        return this.baseMapper.findLastSevenDaysVisitCount(sysUserEntity);
    }
}
