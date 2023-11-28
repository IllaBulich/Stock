package com.example.web.controllers;

import com.example.web.models.Stock;
import com.example.web.services.ProductService;
import com.example.web.services.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductService productService;

    @GetMapping("/addProducts")
    public String addProductsToStockGet(
            Model model){
        model.addAttribute("stocks",stockService.findAll());
        model.addAttribute("products",productService.findAll());
        return "stock/add_products";
    }


    @PostMapping("/addProducts")
    public String addProductsToStockPost(
            @RequestParam Long stockId,
            @RequestParam(value = "productIds", required = false) List<Long> productIds,
            @RequestParam List<Integer> quantities)  {
        if (quantities != null) {
            // Создаем новый List<Integer>, содержащий только элементы, не равные null
            List<Integer> nonNullQuantities = quantities.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            log.info("Product ID: " + productIds + ", Quantity: " + nonNullQuantities);
            stockService.addProductsToStock(stockId, productIds, nonNullQuantities);
        }
        return "redirect:/stocks";
    }

    @GetMapping("")
    public String stocksMain( Model model){
        model.addAttribute("stocks",stockService.findAll());
        return "stock/main";
    }
    @GetMapping("/add")
    public String stockAdd(Model  module){
        return "stock/add";
    }

    @PostMapping("/add")
    public String postStockAdd(
            Stock stock,
            Principal principal) {
        stockService.saveStock(principal,stock);
        return "redirect:/stocks";
    }

    @GetMapping("/{id}/edit")
    public String stockEdit(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model  model){
//        User user = immovablesService.getUserByPrincipal(principal);

        if(stockService.getStockById(id) == null)
            return "redirect:/stocks";
//        if(immovables.getUser() != user)
//            return "redirect:/";
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock",stock);
        model.addAttribute("stock_items",stock.getStockItems());
        return "stock/edit";
    }

    @PostMapping("/{id}/edit")
    public String stockUpdate(
            @PathVariable(value = "id") long id,
            Stock stock,
            Principal principal ) throws IOException {
        stockService.editStock(id, principal,stock);
        return "redirect:/stocks";
    }
    @GetMapping("/details/{id}")
    public String stockDetails(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model  model){
        if(stockService.getStockById(id) == null)
            return "redirect:/stocks";
//        User user = immovablesService.getUserByPrincipal(principal);
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock",stock);
        model.addAttribute("stock_items",stock.getStockItems());
        return "stock/details";
    }

    @PostMapping("/{id}/remove")
    public String postStockDelete(
            @PathVariable(value = "id") long id,
            Model model){
        stockService.deleteStock(id);
        return "redirect:/stocks";
    }
    @PostMapping("/{id}/remove_stock_item")
    public String postStockItemDelete(
            @PathVariable(value = "id") long stockItemId,
            @RequestParam(value = "num") Integer num,
            Model model){
        log.info("Ban user stockItemId={}; num={}",stockItemId,num);
        stockService.decreaseStockItemQuantity(stockItemId, num);
        return "redirect:/stocks/details/"+stockService.findStockItem(stockItemId).getId();
    }



}
