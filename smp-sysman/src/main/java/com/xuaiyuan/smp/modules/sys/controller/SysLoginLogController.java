package com.xuaiyuan.smp.modules.sys.controller;

import com.wuwenze.poi.ExcelKit;
import com.xuaiyuan.smp.common.annotation.SysLog;
import com.xuaiyuan.smp.common.utils.PageUtils;
import com.xuaiyuan.smp.common.utils.Result;
import com.xuaiyuan.smp.modules.sys.entity.SysLoginLogEntity;
import com.xuaiyuan.smp.modules.sys.service.SysLoginLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author lj
 * @Date 2020/1/14 22:20
 */
@RestController
@RequestMapping("sys/login_log")
public class SysLoginLogController extends AbstractController{
    @Autowired
    private SysLoginLogService sysLoginLogService;

    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("sys:login_log:list")
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = sysLoginLogService.findLoginLogs(params);
        return Result.success().put("page", page);
    }

    @SysLog("删除登录日志")
    @RequiresPermissions("sys:login_log:delete")
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        sysLoginLogService.removeByIds(Arrays.asList(ids));
        return Result.success();
    }
    @GetMapping("excel")
    @RequiresPermissions("sys:login_log:export")
    public void export(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        List list = sysLoginLogService.findLoginLogs(params).getList();
        ExcelKit.$Export(SysLoginLogEntity.class, response).downXlsx(list, false);
    }
}
