//A service is part of the business logic layer of your application. It contains the application's core logic and
//coordinates different components. Services DO NOT handle HTTP requests or directly communicate with clients.
//Services can call repositories for database interaction or make API calls to external services. They help maintain
//a separation of concerns by abstracting business logic away from the controllers.
package com.example.demo.Services;

import com.example.demo.Models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.Map;

@Service
public class StockService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate;
    private Portfolio portfolio;
    private User user;

    @Value("${twelvedata.api.key}")
    private String api_key;

    @Value("${twelvedata.api.url.info}")
    private String api_Url_Info;

    @Value("${twelvedata.api.url.history}")
    private String api_Url_History;

    public StockService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public StockInfo getStockData(String symbol) {

        try {
            // URL for the stock's general information
            String infoUrl = UriComponentsBuilder.fromUriString(api_Url_Info)
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", api_key)
                    .toUriString();

            // Make API call
            ResponseEntity<String> response = restTemplate.getForEntity(infoUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                return objectMapper.readValue(body, StockInfo.class);
            } else {
                throw new RuntimeException("Failed to fetch stock info. Status code: " + response.getStatusCode());
            }
        }catch (Exception e) {
            throw new RuntimeException("Error fetching stock from API: " + e.getMessage());
        }
    }

    public StockHistory getStockHistory(String symbol) {
        try {
            String interval = "1day";
            String historyUrl = UriComponentsBuilder.fromUriString(api_Url_History)
                    .queryParam("symbol", symbol)
                    .queryParam("interval", interval)
                    .queryParam("apikey", api_key)
                    .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(historyUrl, String.class);

            // Add a log here to see the response from the external API
            System.out.println("External API response: " + response.getBody());

            if (response.getStatusCode().is2xxSuccessful()) {
                String body = response.getBody();
                System.out.println("Response body: " + body);
                try {
                    return objectMapper.readValue(body, StockHistory.class);
                } catch (Exception ex) {
                    System.err.println("Deserialization error: " + ex.getMessage());
                    ex.printStackTrace();
                    throw new RuntimeException("Error during deserialization");
                }
            } else {
                throw new RuntimeException("Failed to fetch stock info. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error fetching stock from API: " + e.getMessage());
        }
    }

    public Map<String, Object> sellStock(String symbol, int quantity) {
        //Access stock info in portfolio
        Portfolio.OwnershipDetails ownershipDetails = portfolio.getStock(symbol);

        double saleValue = quantity * ownershipDetails.getStockInfo().getClose();

        //If the user does not have it or does not have enough of it
        if (ownershipDetails == null || ownershipDetails.getQuantity() < quantity) {
            throw new RuntimeException("Sorry, you cannot complete this action.");
        }
        //If the user does have enough of the stock
        else{
            //Update stock quantity
            ownershipDetails.setQuantity(ownershipDetails.getQuantity() - quantity);
            //Update user balance
            user.setBalance(saleValue + user.getBalance());
            //If after selling the stock they do not have any shares left
            if (ownershipDetails.getQuantity() <= 0) {
                portfolio.removeStock(symbol);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Stock sold successfully.");
        response.put("updatedStock", portfolio.getStock(symbol));
        return response;
    }

    public Map<String, Object> buyStock(String symbol, int quantity, double price) {
        // Fetch stock data from the API
        StockInfo stock = getStockData(symbol);

        if (stock == null) {
            throw new RuntimeException("Stock information could not be retrieved.");
        }

        Portfolio.OwnershipDetails ownershipDetails = portfolio.getStock(symbol);
        double cost = quantity * price;
        if (user.getBalance() < cost) {
            throw new RuntimeException("Insufficient funds, you cannot purchase this stock right now.");
        }

        if (ownershipDetails == null) {
            portfolio.addStock(symbol, quantity, price, stock);
        } else {
            user.setBalance(user.getBalance() - cost);
            ownershipDetails.setQuantity(ownershipDetails.getQuantity() + quantity);
            ownershipDetails.setPurchasePrice(price);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Stock purchased successfully.");
        response.put("updatedStock", portfolio.getStock(symbol)); // Fetch updated stock info
        return response;
    }

    //Test data
    private StockInfo fetchStockFromAPITest(String symbol) {
        // Simulate API data
        if ("AAPL".equalsIgnoreCase(symbol)) {
            return new StockInfo(
                    "AAPL",
                    "Apple Inc.",
                    "USD",
                    "NASDAQ",
                    "XNAS",
                    "USA",
                    "Equity",
                    150.00
            );
        }
        return null;
    }

}
