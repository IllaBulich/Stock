package com.example.web.services;

import com.example.web.exception.ProductNotFoundException;
import com.example.web.models.Product;
import com.example.web.models.Stock;
import com.example.web.models.StockItem;
import com.example.web.repo.ProductRepository;
import com.example.web.repo.StockItemRepository;
import com.example.web.repo.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final StockItemRepository stockItemRepository;

    public List<Stock> findAll(){
        return stockRepository.findAll();
    }


    public void saveStock(Principal principal, Stock stock) {
        log.info("Ban user id={}; Title={}",stock.getId(),stock.getTitle());
        stockRepository.save(stock);
    }

    public Stock getStockById(long id) {
        return stockRepository.findById(id).orElse(null);
    }

    public void editStock(long id, Principal principal, Stock stockNow) {

        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.setTitle(stockNow.getTitle());
        stock.setCity(stockNow.getCity());
        stock.setAddress(stockNow.getAddress());
        stock.setStreet(stockNow.getStreet());
        stock.setNumber_racks(stockNow.getNumber_racks());
        stockRepository.save(stock);

    }

    public void deleteStock(long id) {
        stockRepository.deleteById(id);
    }


    public void decreaseStockItemQuantity(Long stockItemId, int quantityToRemove) {
        Optional<StockItem> optionalStockItem = stockItemRepository.findById(stockItemId);

        if (optionalStockItem.isPresent()) {
            StockItem stockItem = optionalStockItem.get();

            // Уменьшаем количество продуктов в StockItem
            int currentQuantity = stockItem.getQuantity();
            int newQuantity = currentQuantity - quantityToRemove;

            // Если новое количество меньше или равно нулю, удаляем StockItem
            if (newQuantity <= 0) {
                removeStockItem(stockItemId);
            } else {
                stockItem.setQuantity(newQuantity);

                // Сохраняем обновленный StockItem в базу данных
                stockItemRepository.save(stockItem);
            }
        } else {
            // Обработка случая, если StockItem не найден
            // Можно выбросить исключение или обработать иным способом
        }
    }

    public Stock findStockItem(Long stockItemId){
        Optional<StockItem> optionalStockItem = stockItemRepository.findById(stockItemId);

        if (optionalStockItem.isPresent()) {
            StockItem stockItem = optionalStockItem.get();
            return stockItem.getStock();
        }
        return null;
    }
    public void removeStockItem(Long stockItemId) {
        Optional<StockItem> optionalStockItem = stockItemRepository.findById(stockItemId);

        if (optionalStockItem.isPresent()) {
            StockItem stockItem = optionalStockItem.get();

            // Удаляем StockItem из списка StockItems в Stock
            Stock stock = stockItem.getStock();
            stock.getStockItems().remove(stockItem);

            // Сохраняем обновленный Stock в базу данных
            stockRepository.save(stock);

        } else {
            // Обработка случая, если StockItem не найден
            // Можно выбросить исключение или обработать иным способом
        }
    }
    public void addProductsToStock(Long stockId,Map<Long, Integer> map) {


        Optional<Stock> optionalStock = stockRepository.findById(stockId);

        if (optionalStock.isPresent()) {
            Stock stock = optionalStock.get();


            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                Long productId = entry.getKey();
                Integer quantity = entry.getValue();
                // Ваш код обработки key и value
                Optional<Product> optionalProduct = productRepository.findById(productId);

                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();
                    if (checkPush(stock,product,quantity)) {

                        // Создаем StockItem и устанавливаем связи
                        StockItem stockItem = new StockItem();
                        stockItem.setStock(stock);
                        stockItem.setProduct(product);
                        stockItem.setQuantity(quantity);

                        // Добавляем StockItem к списку StockItems в Stock
                        stock.getStockItems().add(stockItem);
                    }else {
                        throw new ProductNotFoundException("недостаточно местая на складе");
                    }
                } else {
                    // Обработка случая, если продукт не найден
                    // Можно выбросить исключение или обработать иным способом
                    throw new ProductNotFoundException("Продукт не найден");
                }
            }

            // Сохраняем обновленный Stock в базу данных
            stockRepository.save(stock);
        } else {
            // Обработка случая, если склад не найден
            // Можно выбросить исключение или обработать иным способом
        }
    }

    private boolean checkPush(Stock stock, Product product, Integer quantity ){
        Float plenum = (float) 0;
        Float plenumNew = (float) (quantity/product.getQuantity_rack());

        for (StockItem stockItem:stock.getStockItems()){
            plenum += (float) stockItem.getQuantity() /
                    stockItem.getProduct().getQuantity_rack();
        }
        return plenum + plenumNew <= stock.getNumber_racks();
    }
}
