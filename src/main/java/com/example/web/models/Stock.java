package com.example.web.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "stocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String city;
    private String street;
    private String address;
    private Integer number_racks;
    private Float  occupancy_status;

    @OneToMany(mappedBy = "stock", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StockItem> stockItems = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) && Objects.equals(title, stock.title) && Objects.equals(city, stock.city) && Objects.equals(street, stock.street) && Objects.equals(address, stock.address) && Objects.equals(stockItems, stock.stockItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, city, street, address);
    }
}
