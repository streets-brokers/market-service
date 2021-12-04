package com.streets.marketsvc.hooks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitHook {
    /**
     * Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitHook.class);

    public void runHooks() {
        SubscriptionHook.subscribe();
    }
}
