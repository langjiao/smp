package com.xuaiyuan.smp.modules.sys.shiro;

import com.xuaiyuan.smp.common.utils.Constant;
import com.xuaiyuan.smp.modules.sys.dao.SysMenuDao;
import com.xuaiyuan.smp.modules.sys.dao.SysUserDao;
import com.xuaiyuan.smp.modules.sys.entity.SysMenuEntity;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description TODO
 * @Author lj
 * @Date 2019/12/17 20:24
 */
@Slf4j
@Component
public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        Long userId = sysUserEntity.getUserId();
        List<String> permsList;
        //超级管理员拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(permsSet);
        return  authorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        log.info("用户名:{},密码：{}", usernamePasswordToken.getUsername(), usernamePasswordToken.getPassword());
        final String username = (String) token.getPrincipal();
        final String password = String.valueOf(usernamePasswordToken.getPassword());
        if (username == null || "".equals(username)) {
            throw new AccountException("用户名为空！");
        }
//        SysUserEntity sysUserEntity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", usernamePasswordToken.getUsername()));
        SysUserEntity sysUserEntity = sysUserDao.queryUserInfoByUserName(username);
        if (sysUserEntity == null) {
            throw new UnknownAccountException("用户名不存在！");
        }
        if ("0".equals(sysUserEntity.getStatus())) {
            throw new DisabledAccountException("用户已被禁用，请联系管理员！");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(sysUserEntity, sysUserEntity.getPassword(), ByteSource.Util.bytes(sysUserEntity.getSalt()), getName());
        return authenticationInfo;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}
