package com.dahuaboke.mpda.tools.product.config;


import com.dahuaboke.mpda.tools.product.ProductToolHandler;
import com.dahuaboke.mpda.tools.product.service.RobotService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * auth: dahua
 * time: 2025/8/27 15:48
 */
//@Configuration
@AutoConfiguration

public class MapperConfig {

    @Bean
    public RobotService robotService() {
        return new RobotService();
    }

    @Bean
    public ProductToolHandler productToolHandler() {
        return new ProductToolHandler();
    }

}
