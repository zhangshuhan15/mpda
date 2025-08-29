package com.dahuaboke.mpda;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.dahuaboke.mpda.tools.product.dao")
public class MpdaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MpdaApplication.class, args);
    }
}
