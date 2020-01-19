package com.xuaiyuan.smp.common.utils;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description TODO
 * @Author lj
 * @Date 2020/1/12 23:26
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:smp.properties"})
@ConfigurationProperties(prefix = "smp")
public class SmpProperties {
    private ShiroProperties shiro = new ShiroProperties();
    private SwaggerProperties swagger = new SwaggerProperties();
    private boolean autoOpenBrowser = true;
    private int maxBatchInsertNum = 1000;
}
