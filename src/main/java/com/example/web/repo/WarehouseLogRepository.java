package com.example.web.repo;

import com.example.web.models.WarehouseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarehouseLogRepository
        extends JpaRepository<WarehouseLog,Long> {

    @Query("SELECT wl.exit_data, wl.selling_price FROM WarehouseLog wl WHERE wl.active = false ")
    List<Object[]> findExitDataAndSellingPriceWhereActiveIsZero();

    @Query("SELECT DATE_FORMAT(wl.entrance_data, '%Y-%m-%d') AS formattedDate, " +
            "SUM(wl.purchase_price * wl.quantity) AS totalSales " +
            "FROM WarehouseLog wl " +
            "WHERE wl.active = true " +
            "GROUP BY formattedDate")
    List<Object[]> getReceiptStatisticsData();

    @Query("SELECT DATE_FORMAT(wl.exit_data, '%Y-%m-%d') AS formattedDate, " +
            "SUM(wl.selling_price * wl.quantity) AS totalSales " +
            "FROM WarehouseLog wl " +
            "WHERE wl.active = false " +
            "GROUP BY formattedDate")
    List<Object[]> getShipmentStatisticsData();
    List<WarehouseLog> findByActive(boolean active);


}
