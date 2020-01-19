package com.xuaiyuan.smp.modules.monitor.service.impl;
import com.xuaiyuan.smp.common.utils.AddressUtil;
import com.xuaiyuan.smp.modules.monitor.entity.OnlineUserEntity;
import com.xuaiyuan.smp.modules.monitor.service.SessionService;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 在线会话管理，可参看
 */
@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionDAO sessionDAO;

    @Override
    public List<OnlineUserEntity> list() {
        String currentSessionId = (String) SecurityUtils.getSubject().getSession().getId();
        List<OnlineUserEntity> list = new ArrayList<>();
        Collection<Session> sessions = sessionDAO.getActiveSessions(); //获取在线会话的集合
        for (Session session : sessions) {
            if (session != null) {
                OnlineUserEntity onlineUserEntity = new OnlineUserEntity();
                SimplePrincipalCollection principalCollection;
                SysUserEntity sysUserEntity;
                //判断此session是否还在登录状态
                if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
                    continue;
                } else {
                    //如果此session正在登录，将此session的数据放入principalCollection集合中，从而可获取登录用户对象数据
                    principalCollection = (SimplePrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                    sysUserEntity = (SysUserEntity) principalCollection.getPrimaryPrincipal();
                    onlineUserEntity.setUid(sysUserEntity.getUserId().toString());
                    onlineUserEntity.setUsername(sysUserEntity.getUsername());
                }
                onlineUserEntity.setId(session.getId().toString());
                onlineUserEntity.setHost(session.getHost());
                onlineUserEntity.setAddress(AddressUtil.getAddress("0:0:0:0:0:0:0:1".equals(session.getHost())?"127.0.0.1":session.getHost()));
                onlineUserEntity.setStartTime(session.getStartTimestamp());
                onlineUserEntity.setEndTime(session.getLastAccessTime());
                long timeout = session.getTimeout();
                onlineUserEntity.setTimeout(timeout);
                onlineUserEntity.setStatus(timeout == 0L ? "0" : "1"); //0在线 1下线
                list.add(onlineUserEntity);
            }
        }
        return list;
    }
    @Override
    public void forceLogout(String sessionId) {
        Session session = sessionDAO.readSession(sessionId);
        session.setTimeout(0);
        session.stop();
        sessionDAO.delete(session);
    }
}
