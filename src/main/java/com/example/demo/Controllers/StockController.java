package com.example.demo.Controllers;

import com.example.demo.Models.StockHistory;
import com.example.demo.Models.StockInfo;
import com.example.demo.Services.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<StockInfo> getStock(@PathVariable String symbol) {
        try {
            StockInfo stockInfo = stockService.getStockData(symbol);
            return ResponseEntity.ok(stockInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/history/{symbol}")
    public ResponseEntity<List<StockHistory.Values.StockDataPoint>> getStockHistory(@PathVariable String symbol) {
        System.out.println("Fetching stock history for symbol: " + symbol);
        try{
            StockHistory stockHistory = stockService.getStockHistory(symbol);

            //Debugging
            if (stockHistory.getMeta() == null) {
                System.out.println("Meta information is missing for symbol: " + symbol);
            }

            if (stockHistory.getValues() == null || stockHistory.getValues().isEmpty()) {
                System.out.println("No historical data (values) available for symbol: " + symbol);
            }
            //End debugging logs

            if (stockHistory != null && stockHistory.getMeta()!=null) {
                return ResponseEntity.ok(stockHistory.getValues());
            } else {
                System.out.println("Stock history is null for symbol: " + symbol);
                return ResponseEntity.status(404).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PostMapping("/buy")
    public ResponseEntity<Map<String, Object>> postStockBuy(
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam double price
    ) {
        if (quantity <= 0) {
            return ResponseEntity.status(400).body(Map.of("error", "Quantity must be greater than zero."));
        }
        if (price < 0) {
            return ResponseEntity.status(400).body(Map.of("error", "Price cannot be negative."));
        }
        try {
            Map<String, Object> response = stockService.buyStock(symbol, quantity, price);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<Map<String, Object>> postStockSell(
            @RequestParam String symbol,
            @RequestParam int quantity
    ) {
        if (quantity <= 0) {
            return ResponseEntity.status(400).body(Map.of("error", "Quantity must be greater than zero."));
        }
        try{
            Map<String, Object> response = stockService.sellStock(symbol, quantity);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }

    }
}