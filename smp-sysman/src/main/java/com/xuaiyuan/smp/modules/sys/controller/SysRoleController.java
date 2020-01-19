package com.xuaiyuan.smp.modules.sys.controller;
import com.xuaiyuan.smp.common.annotation.SysLog;
import com.xuaiyuan.smp.common.utils.PageUtils;
import com.xuaiyuan.smp.common.utils.Result;
import com.xuaiyuan.smp.common.validator.ValidatorUtils;
import com.xuaiyuan.smp.modules.sys.entity.SysRoleEntity;
import com.xuaiyuan.smp.modules.sys.service.SysRoleDeptService;
import com.xuaiyuan.smp.modules.sys.service.SysRoleMenuService;
import com.xuaiyuan.smp.modules.sys.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
	@Autowired
	private SysRoleService sysRoleService;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysRoleDeptService sysRoleDeptService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Result  list(@RequestParam Map<String, Object> params){
		PageUtils page = sysRoleService.queryPage(params);
		return Result.success().put("page", page);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Result select(){
		List<SysRoleEntity> list = sysRoleService.list();
		return Result.success().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public Result info(@PathVariable("roleId") Long roleId){
		SysRoleEntity role = sysRoleService.getById(roleId);
		//查询角色对应的菜单
		List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);
		//查询角色对应的部门
		List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
		role.setDeptIdList(deptIdList);
		return Result.success().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public Result save(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		sysRoleService.saveRole(role);
		return Result.success();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Result update(@RequestBody SysRoleEntity role){
		ValidatorUtils.validateEntity(role);
		sysRoleService.update(role);
		return Result.success();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Result delete(@RequestBody Long[] roleIds){
		sysRoleService.deleteBatch(roleIds);
		return Result.success();
	}
}
