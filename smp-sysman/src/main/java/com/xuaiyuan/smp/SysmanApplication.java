package com.xuaiyuan.smp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;


/*@SpringBootApplication
public class SysmanApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysmanApplication.class, args);
    }

}*/
@SpringBootApplication
public class SysmanApplication extends SpringBootServletInitializer implements WebApplicationInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SysmanApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SysmanApplication.class);
    }

}