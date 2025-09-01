package com.dahuaboke.mpda.bot.tools.config;


import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.service.RobotService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * auth: dahua
 * time: 2025/8/27 15:48
 */
@Configuration
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
