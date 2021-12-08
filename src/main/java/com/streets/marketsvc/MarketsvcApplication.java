package com.streets.marketsvc;

import com.streets.marketsvc.hooks.InitHook;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class MarketsvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketsvcApplication.class, args);
    }

    @Bean(initMethod = "runHooks")
    public InitHook init() {
        return new InitHook();
    }

}


// Pre-run hooks to make the initial requests for the market data & subscribe