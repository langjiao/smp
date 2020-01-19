package com.xuaiyuan.smp.modules.monitor.service;

import com.xuaiyuan.smp.modules.monitor.entity.OnlineUserEntity;

import java.util.List;

/**
 * @author tycoding
 * @date 2019-02-14
 */
public interface SessionService {

    List<OnlineUserEntity> list();

    void forceLogout(String sessionId);
}
