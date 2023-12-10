package com.example.web.controllers;

import com.example.web.models.Product;
import com.example.web.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String productMain(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/main";
    }

    @GetMapping("/add")
    public String productAdd(Model module) {
        return "product/add";
    }

    @PostMapping("/add")
    public String postProductAdd(
            Product product,
            Principal principal) {
        productService.saveProduct(principal, product);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String productEdit(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model model) {
//        User user = immovablesService.getUserByPrincipal(principal);

        if (productService.getProductById(id) == null)
            return "redirect:/products";
//        if(immovables.getUser() != user)
//            return "redirect:/";
        model.addAttribute("product", productService.getProductById(id));
        return "product/edit";
    }

    @PostMapping("/{id}/edit")
    public String productUpdate(
            @PathVariable(value = "id") long id,
            Product product,
            Principal principal) throws IOException {
        productService.editProduct(id, principal, product);
        return "redirect:/products";
    }

    @GetMapping("/details/{id}")
    public String productDetails(
            @PathVariable(value = "id") long id,
            Principal principal,
            Model model) {
        if (productService.getProductById(id) == null)
            return "redirect:/products";
//        User user = immovablesService.getUserByPrincipal(principal);
        model.addAttribute("product", productService.getProductById(id));

        return "product/details";
    }

    @PostMapping("/{id}/remove")
    public String postProductDelete(
            @PathVariable(value = "id") long id,
            Model model) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }


}