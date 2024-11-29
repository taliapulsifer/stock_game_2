package com.example.demo.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private String name;
    private String currency;
    private String exchange;
    private String mic_code;
    private String country;
    private String type;
    private double close;

    // No-argument constructor
    public StockInfo() {
    }

    public StockInfo(String symbol, String name, double close) {
        this.symbol = symbol;
        this.name = name;
        this.close = close;
    }

    public StockInfo(String symbol, String name, String currency, String exchange, String mic_code, String country,
                     String type, double close) {
        this.symbol = symbol;
        this.name = name;
        this.currency = currency;
        this.exchange = exchange;
        this.mic_code = mic_code;
        this.country = country;
        this.type = type;
        this.close = close;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExchange() {
        return exchange;
    }

    public String getMic_code() {
        return mic_code;
    }

    public String getCountry() {
        return country;
    }

    public String getType() {
        return type;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }


}
