package org.example.customer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.customer.config.VNPayConfig;
import org.example.library.dto.OrderDto;
import org.example.library.dto.PaymentRestDTO;
import org.example.library.model.Order;
import org.example.library.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/customer/checkout")
public class VNPayController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create_payment")
    public ResponseEntity<?> createPayment(@RequestBody  OrderDto orderDto, @CookieValue(value = "userId", required = false) String userIdCookie
            , HttpServletRequest request) throws UnsupportedEncodingException {
        System.out.println("createPayment method called");
        System.out.println("Order Data: " + orderDto);

        Long userId = Long.valueOf(userIdCookie);
        OrderDto createdOrder = orderService.createOrder(orderDto, userId);

        long amount = (long) orderDto.getTotalPrice();
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(request);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Nhân với 100
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "NCB");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (fieldNames.indexOf(fieldName) < fieldNames.size() - 1) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }

        // Tính toán giá trị SecureHash
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.US_ASCII.toString()));
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();

        PaymentRestDTO paymentRestDTO = new PaymentRestDTO();
        paymentRestDTO.setStatus("OK");
        paymentRestDTO.setMessage("Successfully");
        paymentRestDTO.setURL(paymentUrl);
        System.out.println("paymentUrl: " + paymentUrl);
        System.out.println("vnp_SecureHash: " + vnp_SecureHash);
        System.out.println("vnp_Params: " + vnp_Params);
        return ResponseEntity.ok(paymentRestDTO);
    }

    @GetMapping("/vnpay_return")
    public String vnPayReturn(HttpServletRequest request, HttpSession session, Model model) throws UnsupportedEncodingException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHash");

        String signValue = VNPayConfig.hashAllFields(fields);

        String paymentStatus;
        String message;

        if (signValue.equals(vnp_SecureHash)) {
            String responseCode = fields.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                paymentStatus = "success";
                message = "Payment Successful!";


            } else {
                paymentStatus = "failure";
                message = "Payment Failed: " + responseCode;
            }
        } else {
            paymentStatus = "failure";
            message = "Invalid signature";
        }

        // Sử dụng UriComponentsBuilder để xây dựng URL
        String redirectUrl = UriComponentsBuilder.fromUriString("/customer/cart")
                .queryParam("paymentStatus", paymentStatus)
                .queryParam("message", message)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();

        // Chuyển hướng đến trang checkout
        return "redirect:" + redirectUrl;
    }
}
