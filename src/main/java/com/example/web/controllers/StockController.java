package com.example.web.controllers;

import com.example.web.models.Product;
import com.example.web.models.ProductForm;
import com.example.web.models.Stock;
import com.example.web.services.ProductService;
import com.example.web.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;


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
            Model model) {
        List<Product> products = productService.findAll();

        ProductForm productForm = new ProductForm();
        for (Product product : products) {
            productForm.getQuantities().put(product.getId(), 0);
        }

        model.addAttribute("products", products);
        model.addAttribute("productForm", productForm);
        model.addAttribute("stocks", stockService.findAll());

        return "stock/add_products";
    }


    @PostMapping("/addProducts")
    public String addProductsToStockPost(
            @RequestParam Long stockId,
            ProductForm productForm
    ) {

        Map<Long, Integer> Quantities = productForm.getQuantities();
        log.info("Quantities={}", Quantities);
        Quantities.values().removeIf(value -> value == 0);
        log.info("Quantities={}", Quantities);
        stockService.addProductsToStock(stockId, Quantities);

        return "redirect:/stocks";
    }

    @GetMapping("")
    public String stocksMain(Model model) {
        model.addAttribute("stocks", stockService.findAll());
        return "stock/main";
    }

    @GetMapping("/add")
    public String stockAdd(Model module) {
        return "stock/add";
    }

    @PostMapping("/add")
    public String postStockAdd(
            Stock stock,
            Principal principal) {
        stockService.saveStock(principal, stock);
        return "redirect:/stocks";
    }

    @GetMapping("/{id}/edit")
    public String stockEdit(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model model) {
//        User user = immovablesService.getUserByPrincipal(principal);

        if (stockService.getStockById(id) == null)
            return "redirect:/stocks";
//        if(immovables.getUser() != user)
//            return "redirect:/";
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock", stock);
        model.addAttribute("stock_items", stock.getStockItems());
        return "stock/edit";
    }

    @PostMapping("/{id}/edit")
    public String stockUpdate(
            @PathVariable(value = "id") long id,
            Stock stock,
            Principal principal) throws IOException {
        stockService.editStock(id, principal, stock);
        return "redirect:/stocks";
    }

    @GetMapping("/details/{id}")
    public String stockDetails(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model model) {
        if (stockService.getStockById(id) == null)
            return "redirect:/stocks";
//        User user = immovablesService.getUserByPrincipal(principal);
        Stock stock = stockService.getStockById(id);
        model.addAttribute("stock", stock);
        model.addAttribute("stock_items", stock.getStockItems());
        return "stock/details";
    }

    @PostMapping("/{id}/remove")
    public String postStockDelete(
            @PathVariable(value = "id") long id,
            Model model) {
        stockService.deleteStock(id);
        return "redirect:/stocks";
    }

    @PostMapping("/{id}/remove_stock_item")
    public String postStockItemDelete(
            @PathVariable(value = "id") long stockItemId,
            @RequestParam(value = "num") Integer num,
            Model model) {
        log.info("Ban user stockItemId={}; num={}", stockItemId, num);
        String id = String.valueOf(stockService.findStockItem(stockItemId).getId());
        stockService.decreaseStockItemQuantity(stockItemId, num);
        return "redirect:/stocks/details/" + id;
    }


}
