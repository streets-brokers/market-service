package com.streets.marketsvc.mds.trend.analyzers;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import com.streets.marketsvc.mds.trend.results.TrendResult;
import com.streets.marketsvc.mds.trend.util.Calculator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AskPriceAnalyzer implements Analyzer {
    @Override
    public TrendResult analyze(List<RawExchangeData> data) {
        List<Double> movingAverages = Calculator.movingAverage(data.stream().mapToDouble(RawExchangeData::getAskPrice).boxed().collect(Collectors.toList()), 2);
        List<Double> diffs = Calculator.pairwiseDifference(movingAverages);
        int direction = 0;
        if (diffs.stream().allMatch(x -> x > 0)) {
            direction = 1;
        } else if (diffs.stream().allMatch(x -> x < 0)) {
            direction = -1;
        }
        TrendResult tr = new TrendResult();
        tr.setExchange(data.get(0).getXchange());
        tr.setValueType("ASK_PRICE");
        tr.setDirection(direction);

        return tr;
    }
}
