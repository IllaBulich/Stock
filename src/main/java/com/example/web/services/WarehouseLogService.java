package com.example.web.services;


import com.example.web.models.StockItem;
import com.example.web.models.User;
import com.example.web.models.WarehouseLog;
import com.example.web.repo.WarehouseLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WarehouseLogService {

    private final WarehouseLogRepository logRepository;

    public List<WarehouseLog> findAll() {
        return logRepository.findAll();
    }

    public List<WarehouseLog> findByActive(boolean active){return  logRepository.findByActive( active);}



    public List<Object[]> StatisticsData(boolean active){
        List<Object[]> warehouseLogs = new ArrayList<>();
        if(active) warehouseLogs = logRepository.getReceiptStatisticsData();
        else  warehouseLogs = logRepository.getShipmentStatisticsData();

        return warehouseLogs;
    }


    public void pushReceipts(StockItem stockItem){
        if (stockItem != null){
            WarehouseLog warehouseLog = new WarehouseLog();
            warehouseLog.setProduct_name(stockItem.getProduct().getName());
            warehouseLog.setVendor_code(stockItem.getProduct().getVendor_code());
            warehouseLog.setPurchase_price(stockItem.getProduct().getPurchase_price());
            warehouseLog.setStock_title(stockItem.getStock().getTitle());
            warehouseLog.setQuantity(stockItem.getQuantity());
            warehouseLog.setEntrance_data(stockItem.getEntrance_data());
            warehouseLog.setUser(stockItem.getUser());
            warehouseLog.setActive(true);
            logRepository.save(warehouseLog);
        }
    }

    public void pushShipment(StockItem stockItem, User user,Float price,Integer value){
        WarehouseLog warehouseLog = new WarehouseLog();
        warehouseLog.setProduct_name(stockItem.getProduct().getName());
        warehouseLog.setVendor_code(stockItem.getProduct().getVendor_code());
        warehouseLog.setPurchase_price(stockItem.getProduct().getPurchase_price());
        warehouseLog.setStock_title(stockItem.getStock().getTitle());

        warehouseLog.setQuantity(value);
        warehouseLog.setSelling_price(price);
        warehouseLog.setExit_data(LocalDateTime.now());
        warehouseLog.setEntrance_data(stockItem.getEntrance_data());
        warehouseLog.setUser(user);
        warehouseLog.setActive(false);
        logRepository.save(warehouseLog);

    }
}
