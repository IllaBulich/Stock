package com.example.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //product
    private String product_name;
    private String vendor_code;
    private Float  purchase_price;

    //stock
    private String stock_title;


    private int quantity;
    private Float selling_price;
    private LocalDateTime exit_data;
    private LocalDateTime entrance_data;
    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private boolean active;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
