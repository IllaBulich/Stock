package com.example.web.models;

import java.util.HashMap;
import java.util.Map;

public class ProductForm {

    private Map<Long, Integer> quantities = new HashMap<>();

    // Геттеры и сеттеры

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Integer> quantities) {
        this.quantities = quantities;
    }
}