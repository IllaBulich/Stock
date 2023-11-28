package com.example.web.repo;

import com.example.web.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StockRepository extends JpaRepository<Stock,Long> {

}
