package com.streets.marketsvc.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InitHook {
    /**
     * Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitHook.class);
    private final SubscriptionHook initHook;

    @Autowired
    public InitHook(SubscriptionHook initHook) {
        this.initHook = initHook;
    }

    public void runHooks() {
        initHook.subscribe();
    }
}
