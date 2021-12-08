package com.streets.marketsvc.mds.data.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

// The raw response body sent from the exchange service
@Entity
@Table(name = "raw_exchange_data")
@Getter
@Setter
public class RawExchangeData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonAlias({"TICKER", "ticker"})
    private String ticker;

    @JsonAlias({"XCHANGE", "xchange"})
    private String xchange;

    @JsonAlias({"TIMESTAMP", "timestamp"})
    private Long timestamp;

    @JsonAlias({"SELL_LIMIT", "sellLimit"})
    private Integer sellLimit;

    @JsonAlias({"MAX_PRICE_SHIFT", "maxPriceShift"})
    private Double maxPriceShift;

    @JsonAlias({"ASK_PRICE", "askPrice"})
    private Double askPrice;

    @JsonAlias({"BID_PRICE", "bidPrice"})
    private Double bidPrice;

    @JsonAlias({"BUY_LIMIT", "buyLimit"})
    private Integer buyLimit;

    @JsonAlias({"LAST_TRADED_PRICE", "lastTradedPrice"})
    private Double lastTradedPrice;

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        timestamp = now.getTime();
    }

    public RawExchangeData(Long id, String ticker, String xchange, Long timestamp, Integer sellLimit, Double maxPriceShift, Double askPrice, Double bidPrice, Integer buyLimit, Double lastTradedPrice) {
        this.id = id;
        this.ticker = ticker;
        this.xchange = xchange;
        this.timestamp = timestamp;
        this.sellLimit = sellLimit;
        this.maxPriceShift = maxPriceShift;
        this.askPrice = askPrice;
        this.bidPrice = bidPrice;
        this.buyLimit = buyLimit;
        this.lastTradedPrice = lastTradedPrice;
    }

    public RawExchangeData() {
    }


    @Override
    public String toString() {
        return "RawExchangeData{" +
                "id=" + id +
                ", ticker='" + ticker + '\'' +
                ", xchange='" + xchange + '\'' +
                ", timestamp=" + timestamp +
                ", sellLimit=" + sellLimit +
                ", maxPriceShift=" + maxPriceShift +
                ", askPrice=" + askPrice +
                ", bidPrice=" + bidPrice +
                ", buyLimit=" + buyLimit +
                '}' + '\n';
    }
}
