package com.streets.marketsvc;

import com.streets.marketsvc.hooks.InitHook;
import com.streets.marketsvc.hooks.SubscriptionHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class MarketsvcApplication {

    private final InitHook initHook;

    @Autowired
    public MarketsvcApplication(InitHook initHook) {
        this.initHook = initHook;
    }

    public static void main(String[] args) {
        SpringApplication.run(MarketsvcApplication.class, args);
    }

    @Bean(initMethod = "runHooks")
    public InitHook init() {
        return initHook;
    }

}


// Pre-run hooks to make the initial requests for the market data & subscribe