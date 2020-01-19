package com.xuaiyuan.smp.modules.monitor.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuaiyuan.smp.common.annotation.SysLog;
import com.xuaiyuan.smp.common.exception.CustomException;
import com.xuaiyuan.smp.common.utils.Result;
import com.xuaiyuan.smp.modules.monitor.entity.OnlineUserEntity;
import com.xuaiyuan.smp.modules.monitor.service.SessionService;
import com.xuaiyuan.smp.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Description TODO
 * @Author lj
 * @Date 2020/1/8 15:49
 */
@RestController
@RequestMapping("monitor/online")
public class OnlineUserController {
    @Autowired
    private SessionService sessionService;
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params){
        int page = Integer.parseInt(params.get("page")==null?"1":params.get("page").toString());
        int limit = Integer.parseInt(params.get("limit")==null?"10":params.get("limit").toString());
        PageHelper.startPage(page,limit);
        List<OnlineUserEntity> list = sessionService.list();
        PageInfo pageInfo = new PageInfo(list);
        return Result.success().put("page", pageInfo);
    }

    @SysLog("下线用户")
    @GetMapping("/delete")
    @RequiresPermissions("monitor:online:offline")
    public Object delete(@RequestBody String[] sessionIds){
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        if(Arrays.asList(sessionIds).contains(currentSessionId)){
           return Result.error("不可下线自己！");
        }
        try {
            for(String sessionId : sessionIds){
                sessionService.forceLogout(sessionId);
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }
}
