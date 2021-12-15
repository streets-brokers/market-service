package com.streets.marketsvc.mds.data.services;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.data.repositories.RawExchangeDataRepository;
import com.streets.marketsvc.mds.trend.analyzers.AskPriceAnalyzer;
import com.streets.marketsvc.mds.trend.analyzers.BidPriceAnalyzer;
import com.streets.marketsvc.mds.trend.analyzers.TradedPriceAnalyzer;
import com.streets.marketsvc.mds.trend.results.TrendResult;
import com.streets.marketsvc.utils.PropertiesReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ExchangeDataServiceImpl implements ExchangeDataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeDataServiceImpl.class);
    private static final List<String> exchanges = List.of("EXCHANGE1", "EXCHANGE2");
    private final RawExchangeDataRepository repository;
    private final AskPriceAnalyzer askPriceAnalyzer;
    private final BidPriceAnalyzer bidPriceAnalyzer;
    private final TradedPriceAnalyzer tradedPriceAnalyzer;

    @Autowired
    public ExchangeDataServiceImpl(RawExchangeDataRepository repository, AskPriceAnalyzer askPriceAnalyzer, BidPriceAnalyzer bidPriceAnalyzer, TradedPriceAnalyzer tradedPriceAnalyzer) {
        this.repository = repository;
        this.askPriceAnalyzer = askPriceAnalyzer;
        this.bidPriceAnalyzer = bidPriceAnalyzer;
        this.tradedPriceAnalyzer = tradedPriceAnalyzer;
    }

    @Override
    public Iterable<RawExchangeData> listMarketData() {
        return this.repository.findAll();
    }

    @Override
    public Iterable<String> listTickers() {
        return this.repository.listProductTickers();
    }

    @Override
    public RawExchangeData insert(RawExchangeData body) {
        return null;
    }

    @Override
    public Iterable<RawExchangeData> getByTicker(String ticker) {
        return this.repository.findByTicker(ticker);
    }

    @Override
    public List<TrendResult> getMarketAnalysisForProduct(String product) {
        Instant instant = Instant.now();
        long timestamp = instant.toEpochMilli();

        // compute the time for the past 5 mins
        Long elapsedTime = timestamp - TimeUnit.MINUTES.toMillis(5);
        Map<String, List<TrendResult>> results = new HashMap<>();
        exchanges.forEach(x -> {
            results.put(x, this.runAnalysis(x, product, elapsedTime));
        });

        List<TrendResult> trends = new ArrayList<>();
        results.values().forEach(trends::addAll);

        return trends;
    }

    @Override
    public void bulkInsert(List<RawExchangeData> xchangeDataList, String xchange) {
        this.cleanAndSave(xchangeDataList, xchange);
    }

    @Scheduled(initialDelay = 10, fixedDelay = 1000)
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

    private List<TrendResult> runAnalysis(String exchange, String product, Long elapsedTime) {
        List<RawExchangeData> data = (List<RawExchangeData>) this.repository.getRawExchangeDataByTimeRange(product, exchange, elapsedTime);
        List<TrendResult> analysisResults = new ArrayList<>();
        analysisResults.add(askPriceAnalyzer.analyze(data));
        analysisResults.add(bidPriceAnalyzer.analyze(data));
        analysisResults.add(tradedPriceAnalyzer.analyze(data));
        return analysisResults;
    }
}
