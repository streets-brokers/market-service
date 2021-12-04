package com.streets.marketsvc.mds.data.repositories;

import com.streets.marketsvc.mds.data.models.RawExchangeData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawExchangeDataRepository extends CrudRepository<RawExchangeData, Long> {
}
