package com.example.demo.Models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Lob // Large Object Annotation for JSON storage
    @Column(columnDefinition = "TEXT") // Use TEXT to store large JSON strings
    private String holdingsJson;

    @Transient // Do not persist directly; mapped to JSON
    private static Map<String, OwnershipDetails> holdings;

    public Portfolio() {
        this.holdings = new HashMap<>();
    }

    public Map<String, OwnershipDetails> getHoldings() {
        return holdings;
    }

    public void removeStock(String stockID) {
        holdings.remove(stockID);
    }

    public void addStock(String stockID, int quantity, double purchasePrice, StockInfo stockInfo) {
        if(holdings.containsKey(stockID)) {
            OwnershipDetails ownershipDetails = holdings.get(stockID);
            ownershipDetails.setQuantity(ownershipDetails.getQuantity() + quantity);
            ownershipDetails.setPurchasePrice(purchasePrice);
        }
        else{
            holdings.put(stockID, new OwnershipDetails(quantity, purchasePrice, stockInfo));
        }
    }

    public OwnershipDetails getStock(String stockID) {
        return holdings.get(stockID);
    }

    public void listStocks() {
        for (Map.Entry<String, OwnershipDetails> entry : holdings.entrySet()) {
            System.out.println("Stock: " + entry.getKey() + ", " + entry.getValue());
        }
    }

    public double calculatePortfolioValue(){
        double totalValue = 0.0;

        for (Map.Entry<String, OwnershipDetails> entry : holdings.entrySet()) {
            double closePrice = entry.getValue().getStockInfo().getClose();
            double marketValue = entry.getValue().getQuantity() * (closePrice != 0.0 ? closePrice : 0.0);
            totalValue += marketValue;
        }

        return totalValue;
    }

    @PrePersist
    @PreUpdate
    public void updateHoldingsJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.holdingsJson = mapper.writeValueAsString(this.holdings);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert holdings to JSON", e);
        }
    }

    @PostLoad
    public void loadHoldingsFromJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.holdings = mapper.readValue(this.holdingsJson, new TypeReference<Map<String, OwnershipDetails>>() {});
        } catch (IOException e) {
            this.holdings = new HashMap<>();
        }
    }

    // Inner class for stock ownership details
    public static class OwnershipDetails {
        private int quantity;
        private double purchasePrice;
        private StockInfo stockInfo;

        public OwnershipDetails(int quantity, double purchasePrice, StockInfo stockInfo) {
            this.quantity = quantity;
            this.purchasePrice = purchasePrice;
            this.stockInfo = stockInfo;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public void setPurchasePrice(double purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public StockInfo getStockInfo() {
            return stockInfo;
        }

        @Override
        public String toString() {
            return "Quantity: " + quantity +
                    ", Purchase Price: " + purchasePrice +
                    ", Stock Info: " + stockInfo;
        }
    }
}
