package com.example.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductForm {

    private Map<Long, Integer> quantities = new HashMap<>();
    private Map<Long, Float> prices = new HashMap<>();

    // Геттеры и сеттеры

}