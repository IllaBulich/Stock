package com.example.web.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private int quantity;

    // Другие поля и методы доступа

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockItem stockItem = (StockItem) o;
        return quantity == stockItem.quantity && Objects.equals(id, stockItem.id) && Objects.equals(product, stockItem.product) && Objects.equals(stock, stockItem.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, stock, quantity);
    }
}
