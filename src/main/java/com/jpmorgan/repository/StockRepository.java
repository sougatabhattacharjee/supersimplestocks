package com.jpmorgan.repository;

import com.jpmorgan.domain.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by Sougata Bhattacharjee
 * On 10.03.18
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, String> {
    Optional<Stock> findBySymbol(String stockSymbol);
}
