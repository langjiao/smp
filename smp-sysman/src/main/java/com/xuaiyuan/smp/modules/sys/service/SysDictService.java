package com.xuaiyuan.smp.modules.sys.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuaiyuan.smp.common.utils.PageUtils;
import com.xuaiyuan.smp.modules.sys.entity.SysDictEntity;


import java.util.Map;

/**
 * 数据字典
 *
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

