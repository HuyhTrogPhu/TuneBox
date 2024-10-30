package org.example.library.service;

import org.example.library.dto.InstrumentDto;

import java.util.Map;

public interface CartService {

    // Thêm instrument vào giỏ hàng với số lượng
    void addToCart(Long instrumentId, int quantity);

    // Cập nhật số lượng sản phẩm
    void updateQuantity(Long instrumentId, int quantity);

    // Xóa instrument khỏi giỏ hàng
    void removeItem(Long instrumentId);

    // Lấy danh sách instrument và số lượng trong giỏ hàng (theo ID)
    Map<Long, Integer> getCart();

    // Xóa sạch giỏ hàng
    void clearCart();

    // total price of instrument
    double getTotalPrice();

    // total quantity of instrument
    int getTotalQuantity();
}
