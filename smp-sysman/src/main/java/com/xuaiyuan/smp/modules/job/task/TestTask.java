package com.xuaiyuan.smp.modules.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestTask implements ITask{
    @Override
    public void run(String params) {
        log.debug("TestTask定时任务正在执行，参数为：{}", params);
    }
}
