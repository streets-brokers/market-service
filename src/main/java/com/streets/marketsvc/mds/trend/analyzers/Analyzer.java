package com.streets.marketsvc.mds.trend.analyzers;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.trend.results.TrendResult;

import java.util.List;

public interface Analyzer {
    TrendResult analyze(List<RawExchangeData> data);
}
