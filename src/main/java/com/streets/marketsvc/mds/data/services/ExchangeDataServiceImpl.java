package com.streets.marketsvc.mds.data.services;

import com.streets.marketsvc.utils.PropertiesReader;
import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.data.repositories.RawExchangeDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;

@Service
public class ExchangeDataServiceImpl implements ExchangeDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeDataServiceImpl.class);
    private final RawExchangeDataRepository repository;

    @Autowired
    public ExchangeDataServiceImpl(RawExchangeDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<RawExchangeData> listMarketData() {
        return null;
    }

    @Override
    public RawExchangeData insert(RawExchangeData body) {
        return null;
    }

    @Override
    public List<RawExchangeData> getByTicker(String ticker) {
        return null;
    }

    @Override
    public void bulkInsert(List<RawExchangeData> xchangeDataList, String xchange) {
        this.cleanAndSave(xchangeDataList, xchange);
    }

    @PostConstruct
    public void readXchangeState() {
        RestTemplate restTemplate = new RestTemplate();
        this.readXchangeOneState(restTemplate);
        this.readXchangeTwoState(restTemplate);
    }

    private void readXchangeOneState(RestTemplate restTemplate) {
        String baseURL = PropertiesReader.getProperty("EXCHANGE1_BASE_URL");
        this.makeAPICall(restTemplate, baseURL, "EXCHANGE1");
    }

    private void readXchangeTwoState(RestTemplate restTemplate) {
        String baseURL = PropertiesReader.getProperty("EXCHANGE2_BASE_URL");
        this.makeAPICall(restTemplate, baseURL, "EXCHANGE2");
    }

    private void makeAPICall(RestTemplate restTemplate, String baseURL, String xchange) {
        String uri = baseURL + "/md";
        LOGGER.info("Reading exchange state: " + uri);
        try {
            RawExchangeData[] initialState = restTemplate.getForObject(uri, RawExchangeData[].class);
            if (initialState != null && initialState.length > 0) {
                this.cleanAndSave(List.of(initialState), xchange);
            }

        } catch (RestClientException e) {
            LOGGER.info("Could not read data from exchange: " + uri);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private void cleanAndSave(List<RawExchangeData> xchangeDataList, String xchange) {
        if (xchangeDataList != null && xchangeDataList.size() > 0) {
            Instant instant = Instant.now();
            Long timestamp = instant.toEpochMilli();
            for (RawExchangeData data : xchangeDataList) {
                LOGGER.info(data.toString());
                data.setXchange(xchange);
                data.setTimestamp(timestamp);
            }
            this.repository.saveAll(xchangeDataList);
        }
    }
}
