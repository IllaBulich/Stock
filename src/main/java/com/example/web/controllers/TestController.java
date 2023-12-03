package com.example.web.controllers;

import com.example.web.models.Product;
import com.example.web.models.ProductForm;
import com.example.web.services.ProductService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class TestController {
    @Autowired
    private ProductService productService;
    @GetMapping("/productsss")
    public String showProductList(Model model) {
        // Замените этот код на ваш способ получения списка продуктов из базы данных
        List<Product> products = productService.findAll();

        // Создаем объект ProductForm для связывания с формой Thymeleaf
        ProductForm productForm = new ProductForm();
        for (Product product : products) {
            productForm.getQuantities().put(product.getId(), 0);
        }

        model.addAttribute("products", products);
        model.addAttribute("productForm", productForm);
        return "products";
    }

    @PostMapping("/submit")
    public String submitForm(ProductForm productForm) {
        // Обработка данных формы
        // productForm содержит Id продукта и количество для каждого продукта
        // Добавьте ваш код обработки данных, например, сохранение в базу данных
        Map<Long, Integer> Quantities = productForm.getQuantities();
        log.info("Quantities={}",Quantities);
        Quantities.values().removeIf(value -> value == 0);
        log.info("Quantities={}",Quantities);

        return "redirect:/products";
    }
}
