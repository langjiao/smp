package com.xuaiyuan.smp.modules.job.utils;

import com.xuaiyuan.smp.common.utils.SpringContextUtil;
import com.xuaiyuan.smp.modules.job.entity.ScheduleJobEntity;
import com.xuaiyuan.smp.modules.job.entity.ScheduleJobLogEntity;
import com.xuaiyuan.smp.modules.job.service.ScheduleJobLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Description TODO
 * @Author lj
 * @Date 2020/1/3 15:58
 */
@Slf4j
public class ScheduleJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ScheduleJobEntity scheduleJob =(ScheduleJobEntity)jobDataMap.get(ScheduleJobEntity.JOB_PARAM_KEY);
        //获取spring bean
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtil.getBean("scheduleJobLogService");
        //数据库保存执行记录
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setJobId(scheduleJob.getJobId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setParams(scheduleJob.getParams());
        log.setCreateTime(new Date());
        //任务开始时间
        long startTime = System.currentTimeMillis();
        try {
            //执行任务
            this.log.debug("任务准备执行，任务ID：" + scheduleJob.getJobId());
            Object target = SpringContextUtil.getBean(scheduleJob.getBeanName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, scheduleJob.getParams());

            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int)times);
            //任务状态    0：成功    1：失败
            log.setStatus(0);
            this.log.debug("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
        } catch (Exception e) {
            this.log.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            //任务执行总时长
            long times = System.currentTimeMillis() - startTime;
            log.setTimes((int)times);
            //任务状态    0：成功    1：失败
            log.setStatus(1);
            log.setError(StringUtils.substring(e.toString(), 0, 2000));
        }finally {
            scheduleJobLogService.save(log);
        }
    }
}
