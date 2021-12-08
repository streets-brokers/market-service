package com.streets.marketsvc.mds.data.repositories;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawExchangeDataRepository extends CrudRepository<RawExchangeData, Long> {
    @Query(value = "SELECT * FROM raw_exchange_data  WHERE ticker = ?1 ORDER BY timestamp DESC LIMIT 2", nativeQuery = true)
    Iterable<RawExchangeData> findByTicker(String ticker);

    @Query(value = "SELECT DISTINCT(ticker) FROM raw_exchange_data", nativeQuery = true)
    Iterable<String> listProductTickers();
}
