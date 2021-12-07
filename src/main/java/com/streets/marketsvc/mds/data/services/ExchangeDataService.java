package com.streets.marketsvc.mds.data.services;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExchangeDataService {

    // TODO: (romeo) pass in parameters so specify the time range to fetch
    List<RawExchangeData> listMarketData();

    RawExchangeData insert(RawExchangeData body);

    List<RawExchangeData> getByTicker(String ticker);

    void bulkInsert(List<RawExchangeData> responses, String xchange);
}