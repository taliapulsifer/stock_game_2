package com.example.demo;

import com.example.demo.Models.Portfolio;
import com.example.demo.Models.User;
import com.example.demo.Models.StockInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockGameApplicationTests {

    private User user;
    private Portfolio portfolio;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();
        user = new User("user1", 1000.0, portfolio);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testBuyStock_Success() {
        StockInfo stockInfo = new StockInfo("AAPL", "Apple", 150.0);

        boolean result = user.buyStock("AAPL", 2, 150.0, stockInfo);

        assertTrue(result, "Stock purchase should succeed");
        assertEquals(700.0, user.getBalance(), 0.01, "Balance should be reduced correctly");
        assertNotNull(portfolio.getStock("AAPL"), "Stock should be added to portfolio");
        assertEquals(2, portfolio.getStock("AAPL").getQuantity(), "Quantity should be updated correctly");
    }

    @Test
    void testBuyStock_InsufficientFunds() {
        StockInfo stockInfo = new StockInfo("AAPL", "Apple", 150.0);

        boolean result = user.buyStock("AAPL", 10, 150.0, stockInfo);

        assertFalse(result, "Stock purchase should fail with insufficient funds");
        assertEquals(1000.0, user.getBalance(), 0.01, "Balance should remain unchanged");
        assertNull(portfolio.getStock("AAPL"), "Stock should not be added to portfolio");
    }

    @Test
    void testSellStock_Success() {
        StockInfo stockInfo = new StockInfo("AAPL", "Apple", 150.0);
        user.buyStock("AAPL", 5, 150.0, stockInfo);

        boolean result = user.sellStock("AAPL", 3, 200.0);

        assertTrue(result, "Stock sale should succeed");
        assertEquals(1600.0, user.getBalance(), 0.01, "Balance should be updated correctly after sale");
        assertEquals(2, portfolio.getStock("AAPL").getQuantity(), "Remaining quantity should be updated correctly");
    }

    @Test
    void testSellStock_InsufficientStock() {
        StockInfo stockInfo = new StockInfo("AAPL", "Apple", 150.0);
        user.buyStock("AAPL", 2, 150.0, stockInfo);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                user.sellStock("AAPL", 3, 200.0), "Selling more stock than owned should throw an exception");

        assertEquals("Insufficient stock", exception.getMessage());
        assertEquals(700.0, user.getBalance(), 0.01, "Balance should remain unchanged");
        assertEquals(2, portfolio.getStock("AAPL").getQuantity(), "Quantity should remain unchanged");
    }

    @Test
    void testSellStock_RemoveStockWhenQuantityZero() {
        StockInfo stockInfo = new StockInfo("AAPL", "Apple", 150.0);
        user.buyStock("AAPL", 2, 150.0, stockInfo);

        boolean result = user.sellStock("AAPL", 2, 200.0);

        assertTrue(result, "Stock sale should succeed");
        assertEquals(1400.0, user.getBalance(), 0.01, "Balance should be updated correctly after sale");
        assertNull(portfolio.getStock("AAPL"), "Stock should be removed from portfolio when quantity is zero");
    }

    @Test
    void testCalculatePortfolioValue() {
        StockInfo stockInfo1 = new StockInfo("AAPL", "Apple", 150.0);
        StockInfo stockInfo2 = new StockInfo("MSFT", "Microsoft", 300.0);

        user.buyStock("AAPL", 3, 150.0, stockInfo1);
        user.buyStock("MSFT", 2, 300.0, stockInfo2);

        double portfolioValue = portfolio.calculatePortfolioValue();

        assertEquals(1050.0, portfolioValue, 0.01, "Portfolio value should match the total of stock values");
    }
}

