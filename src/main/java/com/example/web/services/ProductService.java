package com.example.web.services;

import com.example.web.models.Product;
import com.example.web.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public void saveProduct(Principal principal, Product product) {
        log.info("Ban user id={}; name={}", product.getId(), product.getName());
        productRepository.save(product);
    }

    public Product getStockById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void editStock(long id, Principal principal, Product productNow) {

        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productNow.getName());
        product.setDescription(productNow.getDescription());
        product.setPurchase_price(productNow.getPurchase_price());
        product.setVendor_code(productNow.getVendor_code());
        product.setQuantity_rack(productNow.getQuantity_rack());
        productRepository.save(product);

    }

    public void deleteStock(long id) {
        productRepository.deleteById(id);
    }


}
