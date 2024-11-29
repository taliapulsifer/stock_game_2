package com.example.demo.Repositories;

import com.example.demo.Models.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public class PortfolioRepo {
    public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {}

}
