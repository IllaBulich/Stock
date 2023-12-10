package com.example.web.controllers;

import com.example.web.models.Product;
import com.example.web.models.ProductForm;
import com.example.web.models.StockItem;
import com.example.web.services.ProductService;
import com.example.web.services.StockItemService;
import com.example.web.services.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/items")
public class StockItemController {

    @Autowired
    private StockService stockService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockItemService stockItemService;
    @GetMapping("/sales-products")
    public String salesProductsGet(Model model){
        List<StockItem> items = stockItemService.findAll();

        ProductForm productForm = new ProductForm();
        for (StockItem stockItem : items) {
            productForm.getQuantities().put(stockItem.getId(), 0);
            productForm.getPrices().put(stockItem.getId(),stockItem.getProduct().getPurchase_price());
        }
        model.addAttribute("productForm", productForm);
        model.addAttribute("items",items);
        return "item/sales_products";
    }
    @PostMapping("/sales-products")
    public String salesProductsPost(
            ProductForm productForm,
            Principal principal
    ) {

        Map<Long, Integer> Quantities = productForm.getQuantities();
        Map<Long, Float> Prices = productForm.getPrices();
        log.info("Quantities={}", Quantities);
        log.info("Prices={}", Prices);
//        Quantities.values().removeIf(value -> value == 0);
        Iterator<Map.Entry<Long, Integer>> quantityIterator = Quantities.entrySet().iterator();
        while (quantityIterator.hasNext()) {
            Map.Entry<Long, Integer> entry = quantityIterator.next();
            if (entry.getValue() == 0) {
                quantityIterator.remove();
                // Удаление соответствующего элемента из priceMap
                Prices.remove(entry.getKey());
            }
        }
        log.info("Quantities={}", Quantities);
        log.info("Prices={}", Prices);
        stockItemService.saleStockItems(Quantities,Prices,principal);
        return "redirect:/stocks";
    }


    @GetMapping("/add-products")
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

        return "item/add_products";
    }


    @PostMapping("/add-products")
    public String addProductsToStockPost(
            @RequestParam Long stockId,
            ProductForm productForm,
            Principal principal
    ) {

        Map<Long, Integer> Quantities = productForm.getQuantities();
        log.info("Quantities={}", Quantities);
        Quantities.values().removeIf(value -> value == 0);
        stockItemService.addProductsToStock(stockId, Quantities, principal);

        return "redirect:/stocks";
    }

    @PostMapping("/{id}/remove_stock_item")
    public String postStockItemDelete(
            @PathVariable(value = "id") long stockItemId,
            @RequestParam(value = "num") Integer num,
            Model model) {
        log.info("Ban user stockItemId={}; num={}", stockItemId, num);
        String id = String.valueOf(stockItemService.findStockItem(stockItemId).getId());
        stockItemService.decreaseStockItemQuantity(stockItemId, num);
        return "redirect:/stocks/details/" + id;
    }
}
