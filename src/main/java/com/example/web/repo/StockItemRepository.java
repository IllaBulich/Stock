package com.example.web.repo;

import com.example.web.models.Stock;
import com.example.web.models.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockItemRepository extends JpaRepository<StockItem,Long> {
}
