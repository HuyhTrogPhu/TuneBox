package org.example.library.service.implement;

import org.example.library.model.Instrument;
import org.example.library.repository.InstrumentRepository;
import org.example.library.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;

@Service
@SessionScope
public class CartServiceImp implements CartService {


    @Autowired
    private InstrumentRepository instrumentRepository;

    private HashMap<Long, Integer> items = new HashMap<>();

    @Override
    public void addToCart(Long instrumentId, int quantity) {
        items.put(instrumentId, items.getOrDefault(instrumentId, 0) + quantity);
        // In ra giỏ hàng sau khi thêm sản phẩm
        System.out.println("Giỏ hàng sau khi thêm sản phẩm: " + items.toString());
    }

    @Override
    public void updateQuantity(Long instrumentId, int quantity) {
        if (items.containsKey(instrumentId)) {
            items.put(instrumentId, quantity);
        } else {
            throw new RuntimeException("Instrument not found in cart");
        }
    }

    @Override
    public void removeItem(Long instrumentId) {
        items.remove(instrumentId);
    }

    @Override
    public Map<Long, Integer> getCart() {
        // In ra giỏ hàng hiện tại và thông tin chi tiết của từng sản phẩm
        System.out.println("Giỏ hàng hiện tại: " + items);
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Long instrumentId = entry.getKey();
            Integer quantity = entry.getValue();
            Instrument instrument = instrumentRepository.findById(instrumentId).orElse(null);
            if (instrument != null) {
                System.out.println("Sản phẩm ID: " + instrumentId +
                        ", Tên: " + instrument.getName() +
                        ", Giá: " + instrument.getCostPrice() +
                        ", Số lượng: " + quantity);
            } else {
                System.out.println("Sản phẩm ID: " + instrumentId + " không tìm thấy.");
            }
        }
        return new HashMap<>(items);
    }

    @Override
    public void clearCart() {
        items.clear();
    }

    @Override
    public double getTotalPrice() {
        double totalPrice = 0.0;
        for (Map.Entry<Long, Integer> entry : items.entrySet()) {
            Instrument instrument = instrumentRepository.findById(entry.getKey()).orElse(null);
            if (instrument != null) {
                totalPrice += instrument.getCostPrice() * entry.getValue();
            }
        }
        return totalPrice;
    }

    @Override
    public int getTotalQuantity() {
        int totalQuantity = 0;
        for (Integer quantity : items.values()) {
            totalQuantity += quantity;
        }
        return totalQuantity;
    }
}