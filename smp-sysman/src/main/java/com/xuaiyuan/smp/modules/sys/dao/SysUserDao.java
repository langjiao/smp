package com.xuaiyuan.smp.modules.sys.dao;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 系统用户
 *
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);


	/**
	 * 根据用户名查询用户的信息
	 */
	@Select("select * from sys_user where username = #{userName}")
	SysUserEntity queryUserInfoByUserName(String userName);

	/**
	 * 查询用户列表分页-mybatis plus 分页
	 */
	@Select("select * from sys_user ${ew.customSqlSegment}")
	IPage<SysUserEntity> pageList(IPage<SysUserEntity> page, @Param(Constants.WRAPPER) Wrapper<SysUserEntity> queryWrapper);

	/**
	 * 查询用户列表分页-pagehelper 分页
	 */
	List<SysUserEntity> pageList2();

}
