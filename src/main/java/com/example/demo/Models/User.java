package com.example.demo.Models;
import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private String userID;

    private String name;
    private double balance;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public User() {
        this.userID = "";
        this.name = "";
        this.balance = 0.0;
        this.portfolio = new Portfolio(); // Ensures every user has a portfolio by default
    }

    public User(String userID, double balance, Portfolio portfolio) {
        this.userID = userID;
        this.balance = balance;
        this.portfolio = portfolio;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deductBalance(double amount) {
        if(balance >= amount) {
            balance -= amount;
        }
        else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public boolean buyStock(String stockID, int quantity, double purchasePrice, StockInfo stockInfo){
        double totalCost = purchasePrice * quantity;

        if(balance < totalCost) {
            return false;
        }
        try{
            deductBalance(totalCost);
            portfolio.addStock(stockID, quantity, purchasePrice, stockInfo);
            return true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean sellStock(String stockID, int quantity, double currentPrice){
        Portfolio.OwnershipDetails details = portfolio.getStock(stockID);
        if(details == null || details.getQuantity() < quantity){
            throw new IllegalArgumentException("Insufficient stock");
        }
        try{
            double revenue = currentPrice * quantity;
            details.setQuantity(details.getQuantity() - quantity);
            if(details.getQuantity() == 0){
                portfolio.removeStock(stockID);
            }
            addBalance(revenue);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error during stock sale", e);
        }
    }

}
