package com.xuaiyuan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuaiyuan.smp.modules.sys.dao.SysUserDao;
import com.xuaiyuan.smp.modules.sys.entity.SysUserEntity;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@MapperScan("com.xuaiyuan.smp.modules.sys.dao")
class SysmanApplicationTests {

    @Autowired
    private SysUserDao sysUserDao;
    @Test
    public void contextLoads() {
        System.out.println("=======Start=========");
        //mybaits plus默认方式分页
        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
        Page<SysUserEntity> page = new Page<>(2,1);
//        IPage iPage = sysUserDao.pageList(page, queryWrapper);
        IPage iPage = sysUserDao.selectPage(page,queryWrapper);
        System.out.println("总页数"+iPage.getPages());
        System.out.println("总记录数"+iPage.getTotal());
        List<SysUserEntity> records = iPage.getRecords();
        records.forEach(System.out::println);

        System.out.println("========End==========");
    }
    @Test
    public void contextLoads2() {
        System.out.println("=======Start=========");
        //mybaits plus默认方式分页
        PageHelper.startPage(1, 1);
        List<SysUserEntity> userList = sysUserDao.pageList2();
        PageInfo pageInfo = new PageInfo<>(userList);
        System.out.println(pageInfo);
        System.out.println("========End==========");
    }

}
