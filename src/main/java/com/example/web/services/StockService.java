package com.example.web.services;

import com.example.web.exception.ProductNotFoundException;
import com.example.web.models.Product;
import com.example.web.models.Stock;
import com.example.web.models.StockItem;
import com.example.web.models.User;
import com.example.web.repo.ProductRepository;
import com.example.web.repo.StockItemRepository;
import com.example.web.repo.StockRepository;
import com.example.web.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    private final StockItemRepository stockItemRepository;


    public List<Stock> findAll() {
        return stockRepository.findAll();
    }


    public void saveStock(Principal principal, Stock stock) {
        log.info("Ban user id={}; Title={}", stock.getId(), stock.getTitle());
        stockRepository.save(stock);
    }

    public Stock getStockById(long id) {
        return stockRepository.findById(id).orElse(null);
    }

    public void editStock(long id, Principal principal, Stock stockNow) {

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.setTitle(stockNow.getTitle());
        stock.setCity(stockNow.getCity());
        stock.setAddress(stockNow.getAddress());
        stock.setStreet(stockNow.getStreet());
        stock.setNumber_racks(stockNow.getNumber_racks());
        stockRepository.save(stock);

    }

    public void deleteStock(long id) {
        stockRepository.deleteById(id);
    }




}
