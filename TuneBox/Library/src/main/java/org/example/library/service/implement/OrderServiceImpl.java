package org.example.library.service.implement;

import org.example.library.dto.*;
import org.example.library.model.Instrument;
import org.example.library.model.Order;
import org.example.library.model.OrderDetail;
import org.example.library.model.User;
import org.example.library.repository.InstrumentRepository;
import org.example.library.repository.OrderDetailRepository;
import org.example.library.repository.OrderRepository;
import org.example.library.repository.UserRepository;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public List<OrderListDto> getOrderList() {
        return orderRepository.getAllOrderList();
    }

    @Override
    public List<UserIsInvoice> getOrderByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderId));
    }


    @Override
    public OrderDto createOrder(OrderDto orderDto, Long userId) {
        System.out.println("Creating order for user ID: " + userId);
        System.out.println("Order data: " + orderDto);

        // Lấy thông tin người dùng
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setOrderDate(orderDto.getOrderDate());
        order.setDeliveryDate(null);
        order.setTax(orderDto.getTax());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setTotalItems(orderDto.getTotalItem());
        order.setPaymentMethod(orderDto.getPaymentMethod());
        order.setStatus(orderDto.getStatus());
        order.setAddress(orderDto.getAddress());
        order.setShippingMethod(orderDto.getShippingMethod());
        order.setPhoneNumber(orderDto.getPhoneNumber());
        order.setPaymentStatus(orderDto.getPaymentStatus());
        order.setUser(user);

        try {
            // Xử lý các chi tiết đơn hàng
            List<OrderDetail> orderDetails = orderDto.getOrderDetails().stream().map(detailDto -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(detailDto.getQuantity());

                // Lấy nhạc cụ từ database
                Instrument instrument = instrumentRepository.findById(detailDto.getInstrumentId())
                        .orElseThrow(() -> new RuntimeException("Instrument not found with ID: " + detailDto.getInstrumentId()));
                orderDetail.setInstrument(instrument);
                orderDetail.setOrder(order);

                // Cập nhật số lượng của nhạc cụ
                int newQuantity = instrument.getQuantity() - detailDto.getQuantity();
                if (newQuantity < 0) {
                    throw new RuntimeException("Not enough quantity for instrument ID: " + detailDto.getInstrumentId());
                }
                instrument.setQuantity(newQuantity);
                instrumentRepository.save(instrument); // Lưu nhạc cụ với số lượng mới vào cơ sở dữ liệu

                return orderDetail;
            }).collect(Collectors.toList());

            order.setOrderDetails(orderDetails);

            // Lưu đơn hàng vào cơ sở dữ liệu
            Order savedOrder = orderRepository.save(order);

            // Chuyển đổi dữ liệu để trả về DTO
            return mapToDto(savedOrder);
        } catch (RuntimeException e) {
            // Ghi lại lỗi vào log để kiểm tra
            System.err.println("Error creating order: " + e.getMessage());
            throw new RuntimeException("Error creating order: " + e.getMessage());
        }
    }


    @Override
    public OrderDetailInfoDto getOrderDetailByOrderId(Long orderId) {
        // Lấy thông tin chi tiết của đơn hàng (không bao gồm sản phẩm)
        OrderDetailInfoDto orderDetailInfoDto = orderRepository.findOrderDetailByOrderId(orderId);

        // Lấy danh sách sản phẩm của đơn hàng
        List<OrderItemsDto> orderItems = orderRepository.findOrderItemsByOrderId(orderId);

        // Gán danh sách sản phẩm vào DTO
        orderDetailInfoDto.setOrderItems(orderItems);

        return orderDetailInfoDto;
    }

    @Override
    public Double revenueOfDay() {
        return orderRepository.getRevenueOfDay();
    }

    @Override
    public Double revenueOfWeek() {
        return orderRepository.getRevenueOfWeek();
    }

    @Override
    public Double revenueOfMonth() {
        return orderRepository.getRevenueOfMonth();
    }

    @Override
    public Double revenueOfYear() {
        return orderRepository.getRevenueOfYear();
    }

    @Override
    public Double revenueBeforeOfDay() {
        return orderRepository.getRevenueOfBeforeDay();
    }

    @Override
    public Double revenueBeforeOfWeek() {
        return orderRepository.getRevenueOfBeforeWeek();
    }

    @Override
    public Double revenueBeforeOfMonth() {
        return orderRepository.getRevenueOfBeforeMonth();
    }

    @Override
    public Double revenueBeforeOfYear() {
        return orderRepository.getRevenueOfBeforeYear();
    }

    @Override
    public Double revenueByDay(LocalDate date) {
        return orderRepository.getRevenueByDay(date);
    }

    @Override
    public Double revenueBetweenDate(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getRevenueBetweenDate(startDate, endDate);
    }

    @Override
    public Double revenueByWeek(LocalDate date) {
        try {
            return orderRepository.getRevenueByWeek(date);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public Double revenueBetweenWeeks(LocalDate startDate, LocalDate endDate) {
        try {
            return orderRepository.getRevenueBetweenWeek(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public Double revenueByMonth(int year, int month) {
        return orderRepository.getRevenueByMonth(year, month);
    }

    @Override
    public Double revenueBetweenMonths(int year, int startMonth, int endMonth) {
        return orderRepository.getRevenueBetweenMonths(year, startMonth, endMonth);
    }

    @Override
    public Double revenueByYear(int year) {
        return orderRepository.getRevenueByYear(year);
    }

    @Override
    public Double revenueBetweenYears(int startYear, int endYear) {
        return orderRepository.getRevenueBetweenYears(startYear, endYear);
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusUnpaid() {
        return orderRepository.getListOrderByPaymentStatusUnpaid();
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusPaid() {
        return orderRepository.getListOrderByPaymentStatusPaid();
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusConfirmed() {
        return orderRepository.getListOrderByStatusConfirmed();
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusDelivered() {
        return orderRepository.getListOrderByStatusDelivered();
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusDelivering() {
        return orderRepository.getListOrderByStatusDelivering();
    }

    @Override
    public List<StatisticalOrder> getOrdersByStatusCanceled() {
        return orderRepository.getListOrderByStatusCanceled();
    }

    @Override
    public List<StatisticalOrder> getOrdersByPaymentMethodCOD() {
        return orderRepository.getListOrderByPaymentMethodCOD();
    }

    @Override
    public List<StatisticalOrder> getOrdersByPaymentMethodVNPAY() {
        return orderRepository.getListOrderByPaymentMethodVNPAY();
    }

    @Override
    public List<StatisticalOrder> getOrdersByShippingMethodNormal() {
        return orderRepository.getListOrderByShippingMethodNormal();
    }

    @Override
    public List<StatisticalOrder> getOrdersByShippingMethodFast() {
        return orderRepository.getListOrderByShippingMethodFast();
    }




    @Override
    public void updateOrderStatus(Long orderId, String status, LocalDate deliveryDate, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        order.setDeliveryDate(deliveryDate);
        order.setPaymentStatus(paymentStatus);// Cập nhật deliveryDate
        orderRepository.save(order);
    }


    // Hàm hỗ trợ chuyển đổi từ Order sang OrderDto
    public OrderDto mapToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getId());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setDeliveryDate(order.getDeliveryDate());
        orderDto.setTax(order.getTax());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setTotalItem(order.getTotalItems());
        orderDto.setPaymentMethod(order.getPaymentMethod());
        orderDto.setPhoneNumber(order.getPhoneNumber());
        orderDto.setStatus(order.getStatus());
        orderDto.setAddress(order.getAddress());
        orderDto.setShippingMethod(order.getShippingMethod());
        orderDto.setUserId(order.getUser().getId());
        orderDto.setPaymentStatus(order.getPaymentStatus());

        orderDto.setUsername(order.getUser().getUserName());
        orderDto.setEmail(order.getUser().getEmail());
        List<OrderDetailDto> orderDetailDtos = order.getOrderDetails().stream().map(detail -> {
            OrderDetailDto detailDto = new OrderDetailDto();
            detailDto.setId(detail.getId());
            detailDto.setQuantity(detail.getQuantity());
            detailDto.setInstrumentId(detail.getInstrument().getId());
            detailDto.setInstrumentName(detail.getInstrument().getName());
            detailDto.setImage(detail.getInstrument().getImage());
            detailDto.setCostPrice(String.valueOf(detail.getInstrument().getCostPrice()));
            return detailDto;
        }).collect(Collectors.toList());

        orderDto.setOrderDetails(orderDetailDtos);

        return orderDto;
    }
    public boolean updatePaymentStatus(Long orderId, String paymentStatus ) {
        // Logic cập nhật trạng thái thanh toán vào CSDL
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId)); // Sử dụng orElseThrow để lấy giá trị

        // Cập nhật trạng thái thanh toán
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
        return true;
    }



}