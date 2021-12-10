package com.streets.marketsvc.mds.controllers.apis;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.data.services.ExchangeDataService;
import com.streets.marketsvc.mds.data.services.ExchangeDataServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/marketservice")
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    // TODO: (romeo) Field injection is not recommended, prolly change for the service to be passed as a constructor
    // parameter?
    private final ExchangeDataServiceImpl dataService;

    @Autowired
    public Controller(ExchangeDataServiceImpl dataService) {
        this.dataService = dataService;
    }

    @PostMapping("/md/x1")
    public void xchange1DataWebhookHandler(@RequestBody Iterable<RawExchangeData> rawXchangeResponseBody) {
        LOGGER.info(rawXchangeResponseBody.toString());
        // consumes market data and write them to db.
        // TODO: (romeo) Add some pre-processing hook to be run before saving to the db
        this.dataService.bulkInsert((List<RawExchangeData>) rawXchangeResponseBody, "EXCHANGE1");

        // Not really scaling to high volumes of orders this doesn't need write to a queue
    }

    @PostMapping("/md/x2")
    public void xchange2DataWebhookHandler(@RequestBody Iterable<RawExchangeData> rawXchangeResponseBody) {
        LOGGER.info(rawXchangeResponseBody.toString());
        // consumes market data and write them to db.
        // TODO: (romeo) Add some pre-processing hook to be run before saving to the db
        this.dataService.bulkInsert((List<RawExchangeData>) rawXchangeResponseBody, "EXCHANGE2");

        // Not really scaling to high volumes of orders this doesn't need write to a queue
    }

    @GetMapping("/products")
    @ResponseBody
    public Iterable<RawExchangeData> getXchangeData() {
        return dataService.listMarketData();
    }

    @GetMapping("/products/tickers")
    @ResponseBody
    public Iterable<String> listProductTickers() {
        return dataService.listTickers();
    }

    @GetMapping("/products/{ticker}")
    @ResponseBody
    public Iterable<RawExchangeData> getXchangeDataForProduct(@PathVariable String ticker) {
        return dataService.getByTicker(ticker);
    }
}

