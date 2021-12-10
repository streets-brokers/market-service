package com.streets.marketsvc.mds.data.services;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.trend.results.TrendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ExchangeDataService {

    // TODO: (romeo) pass in parameters so specify the time range to fetch
    Iterable<RawExchangeData> listMarketData();
    Iterable<String> listTickers();

    RawExchangeData insert(RawExchangeData body);

    Iterable<RawExchangeData> getByTicker(String ticker);
    List<TrendResult> getMarketAnalysisForProduct(String product);

    void bulkInsert(List<RawExchangeData> responses, String xchange);
}
