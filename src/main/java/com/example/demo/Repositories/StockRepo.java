package com.example.demo.Repositories;

import com.example.demo.Models.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public class StockRepo {
    public interface StockRepository extends JpaRepository<StockInfo, Long> {
        StockInfo findBySymbol(String symbol);
    }

}
