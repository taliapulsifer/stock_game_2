package com.example.demo.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   private Meta meta;
    @JsonProperty("values")
    private List<Values.StockDataPoint> values;
    @JsonProperty("status")
    private String status;


    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Values.StockDataPoint> getValues() {
        return values;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        private String symbol;
        private String interval;
        private String currency;
        private String exchangeTimezone;
        private String exchange;
        private String micCode;
        private String type;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @JsonProperty("exchange_timezone")
        public String getExchangeTimezone() {
            return exchangeTimezone;
        }

        public void setExchangeTimezone(String exchangeTimezone) {
            this.exchangeTimezone = exchangeTimezone;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        @JsonProperty("mic_code")
        public String getMicCode() {
            return micCode;
        }

        public void setMicCode(String micCode) {
            this.micCode = micCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    // Nested class for the stock's history data
    public static class Values {

        // Nested class to represent each data point
        public static class StockDataPoint {
            private String datetime;
            private String open;
            private String high;
            private String low;
            private String close;
            private String volume;

            // Constructor
            public StockDataPoint(String datetime, String open, String high, String low, String close, String volume) {
                this.datetime = datetime;
                this.open = open;
                this.high = high;
                this.low = low;
                this.close = close;
                this.volume = volume;
            }

            // Default constructor
            public StockDataPoint() {
            }

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public String getOpen() {
                return open;
            }

            public void setOpen(String open) {
                this.open = open;
            }

            public String getHigh() {
                return high;
            }

            public void setHigh(String high) {
                this.high = high;
            }

            public String getLow() {
                return low;
            }

            public void setLow(String low) {
                this.low = low;
            }

            public String getClose() {
                return close;
            }

            public void setClose(String close) {
                this.close = close;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }
        }
    }
}