package com.example.web.repo;

import com.example.web.models.WarehouseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseLogRepository
        extends JpaRepository<WarehouseLog,Long> {

}
