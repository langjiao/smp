package com.xuaiyuan.smp.modules.sys.controller;
import com.xuaiyuan.smp.common.annotation.SysLog;
import com.xuaiyuan.smp.common.utils.PageUtils;
import com.xuaiyuan.smp.common.utils.Result;
import com.xuaiyuan.smp.modules.sys.service.SysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 系统日志
 *
 */
@RestController
@RequestMapping("sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Result list(@RequestParam Map<String, Object> params){
		PageUtils page = sysLogService.queryPage(params);
		return Result.success().put("page", page);
	}
	@SysLog("删除系统日志")
	@RequiresPermissions("sys:log:delete")
	@RequestMapping("/delete")
	public Result delete(@RequestBody Long[] ids){
		sysLogService.removeByIds(Arrays.asList(ids));
		return Result.success();
	}
	
}
