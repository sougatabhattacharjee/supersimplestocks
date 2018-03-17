package com.jpmorgan.repository;

import com.jpmorgan.domain.Trade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
@Repository
public interface TradeRepository extends CrudRepository<Trade, Long> {
    List<Trade> findByStockSymbol(String stockSymbol);
}
