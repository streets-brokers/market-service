package com.streets.marketsvc.hooks;

import com.streets.marketsvc.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class SubscriptionHook {
    /**
     * Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionHook.class);

    public void subscribe() {
        String apiKey = PropertiesReader.getProperty("API_KEY");
        String baseURL = PropertiesReader.getProperty("EXCHANGE1_BASE_URL");
        String uri = baseURL + "/md/subscription";
        RestTemplate restTemplate = new RestTemplate();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String webHookURL = "https://streetsb/consumer/md";

        HttpEntity<String> entity = new HttpEntity<>(webHookURL, headers);
        restTemplate.postForObject(uri, entity, String.class);
        LOGGER.info("Subscribing to the webhook api");
    }
}
