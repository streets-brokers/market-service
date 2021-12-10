package com.streets.marketsvc.mds.trend.util;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    public static Double[] cumulativeSum(List<Double> in) {
        Double[] out = new Double[in.size()];
        Double total = (double) 0;
        for (int i = 0; i < in.size(); i++) {
            total += in.get(i);
            out[i] = total;
        }
        return out;
    }

    public static List<Double> movingAverage(List<Double> in, int window) {
        List<Double> averages = new ArrayList<>();
        for (int i = 0; i + window <= in.size(); i++) {
            Double sum = (double) 0;
            for (int j = i; j < i + window; j++) {
                sum += in.get(j);
            }
            Double average = sum / 5;
            averages.add(average);
        }
        return averages;
    }

    public static List<Double> pairwiseDifference(List<Double> in)
    {
        List<Double> diffs = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {

            // difference between
            // consecutive numbers
            diffs.add(in.get(i) - in.get(i + 1));
        }
        return diffs;
    }
}
