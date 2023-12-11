package com.example.web.services;

import com.example.web.exception.ProductNotFoundException;
import com.example.web.models.Product;
import com.example.web.models.Stock;
import com.example.web.models.StockItem;
import com.example.web.models.User;
import com.example.web.repo.ProductRepository;
import com.example.web.repo.StockItemRepository;
import com.example.web.repo.StockRepository;
import com.example.web.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockItemService {

    private final StockItemRepository stockItemRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WarehouseLogService logService;

    public List<StockItem> findAll() {
        return stockItemRepository.findAll();
    }

    public void saleStockItems (Map<Long, Integer> quantities,Map<Long, Float> prices, Principal principal){

        for (Map.Entry<Long, Integer> entry : quantities.entrySet()) {
            Long key = entry.getKey();
            Integer value = entry.getValue();
            Float price = prices.get(key);
            log.info("key={}, value={}, price ={}",key,value,price);
            Optional<StockItem> optionalStockItem = stockItemRepository.findById(key);
            if (optionalStockItem.isPresent()) {
                StockItem stockItem = optionalStockItem.get();
                logService.pushShipment(stockItem,getUserByPrincipal(principal),price,value);
            }
            decreaseStockItemQuantity(key,value);

        }

    }

    public void addProductsToStock(Long stockId, Map<Long, Integer> map,Principal principal) {

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
                    if (checkPush(stock, product, quantity)) {

                        // Создаем StockItem и устанавливаем связи
                        StockItem stockItem = new StockItem();
                        stockItem.setStock(stock);
                        stockItem.setProduct(product);
                        stockItem.setQuantity(quantity);
                        stockItem.setEntrance_data(LocalDateTime.now());
                        stockItem.setUser(getUserByPrincipal(principal));
                        // Добавляем StockItem к списку StockItems в Stock
                        stock.getStockItems().add(stockItem);
                        logService.pushReceipts(stockItem);
                    } else {
                        throw new ProductNotFoundException("Недостаточно местая на складе");
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
            throw new ProductNotFoundException("Cклад не найден");
        }
    }
    private boolean checkPush(Stock stock, Product product, Integer quantity) {
        Float plenum = (float) 0;
        Float plenumNew = (float) (quantity / product.getQuantity_rack());

        for (StockItem stockItem : stock.getStockItems()) {
            plenum += (float) stockItem.getQuantity() /
                    stockItem.getProduct().getQuantity_rack();
        }
        if (plenum + plenumNew <= stock.getNumber_racks())
            stock.setOccupancy_status(plenum + plenumNew);
        return plenum + plenumNew <= stock.getNumber_racks();
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void decreaseStockItemQuantity(Long stockItemId, int quantityToRemove) {
        Optional<StockItem> optionalStockItem = stockItemRepository.findById(stockItemId);

        if (optionalStockItem.isPresent()) {
            StockItem stockItem = optionalStockItem.get();
            Stock stock = stockItem.getStock();
            stock.setOccupancy_status(stock.getOccupancy_status()-(quantityToRemove/stockItem.getProduct().getQuantity_rack()));
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

    public Stock findStockItem(Long stockItemId) {
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


}
