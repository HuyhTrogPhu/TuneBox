package org.example.ecommerceadmin.controller;

import org.example.library.dto.InstrumentSalesDto;
import org.example.library.dto.StatisticalInstrumentDto;
import org.example.library.dto.UserRevenueInfo;
import org.example.library.dto.UserSell;
import org.example.library.service.InstrumentService;
import org.example.library.service.OrderService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/e-statistical")
public class StatisticalController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private InstrumentService instrumentService;

    // get list user sell the most
    @GetMapping("/user-sell-most")
    public ResponseEntity<?> getUserSellTheMost() {
        try {
            List<UserSell> userSells = userService.getUserSellTheMost();
            return ResponseEntity.ok(userSells);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get revenue be long top 1 user
    @GetMapping("/top1-user-sell-most")
    public ResponseEntity<?> getTopUserRevenueInfo() {
        try {
            UserSell revenueInfo = userService.getTop1UserRevenueInfo();
            return ResponseEntity.ok(revenueInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // get list user sell the least
    @GetMapping("/user-sell-least")
    public ResponseEntity<?> getUserSellTheLeast() {
        try {
            List<UserSell> userSells = userService.getUserBuyTheLeast();
            return ResponseEntity.ok(userSells);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get top 1 user sell the least
    @GetMapping("/top1-user-sell-least")
    public ResponseEntity<?> getTopUserSellTheLeast() {
        try {
            UserSell userSells = userService.getTop1UserBuyTheLeast();
            return ResponseEntity.ok(userSells);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get user not sell
    @GetMapping("/user-not-sell")
    public ResponseEntity<?> getUserNotSell() {
        try {
            List<UserSell> userSells = userService.getUserNotSell();
            return ResponseEntity.ok(userSells);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // statistical revenue currently
    @GetMapping("/revenue-currently")
    public ResponseEntity<?> getRevenueCurrently() {
        try {
            Double revenueOfDay = orderService.revenueOfDay();
            Double revenueOfWeek = orderService.revenueOfWeek();
            Double revenueOfMonth = orderService.revenueOfMonth();
            Double revenueOfYear = orderService.revenueOfYear();

            // Tạo object hoặc Map chứa kết quả trả về
            Map<String, Double> revenue = new HashMap<>();
            revenue.put("revenueOfDay", revenueOfDay != null ? revenueOfDay : 0.0);
            revenue.put("revenueOfWeek", revenueOfWeek != null ? revenueOfWeek : 0.0);
            revenue.put("revenueOfMonth", revenueOfMonth != null ? revenueOfMonth : 0.0);
            revenue.put("revenueOfYear", revenueOfYear != null ? revenueOfYear : 0.0);

            // Trả về response với dữ liệu doanh thu
            return ResponseEntity.ok(revenue);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // statistical instrument
    @GetMapping("/instrument")
    public ResponseEntity<?> getStatisticalInstrument(){
        try {

            // get id and name instrument
            List<StatisticalInstrumentDto> statisticalInstruments = instrumentService.getIdAndNameInstrument();

            // Tạo object hoặc Map chứa kết quả trả về
            Map<String, List<StatisticalInstrumentDto>> statisticalInstrument = new HashMap<>();
            statisticalInstrument.put("statisticalInstrument", statisticalInstruments);


            InstrumentSalesDto instrumentSalesTheMostOfDay = instrumentService.instrumentSalesTheMostOfDay();
            InstrumentSalesDto instrumentSalesTheMostOfWeek = instrumentService.instrumentSalesTheMostOfWeek();
            InstrumentSalesDto instrumentSalesTheMostOfMonth = instrumentService.instrumentSalesTheMostOfMonth();
            InstrumentSalesDto instrumentSalesTheLeastOfDay = instrumentService.instrumentSalesTheLeastOfDay();
            InstrumentSalesDto instrumentSalesTheLeastOfWeek = instrumentService.instrumentSalesTheLeastOfWeek();
            InstrumentSalesDto instrumentSalesTheLeastOfMonth = instrumentService.instrumentSalesTheLeastOfMonth();

            // Tạo object hoặc Map chứa kết quả trả về
            Map<String, InstrumentSalesDto> instrumentSales = new HashMap<>();
            instrumentSales.put("instrumentSalesTheMostOfDay", instrumentSalesTheMostOfDay);
            instrumentSales.put("instrumentSalesTheMostOfWeek", instrumentSalesTheMostOfWeek);
            instrumentSales.put("instrumentSalesTheMostOfMonth", instrumentSalesTheMostOfMonth);
            instrumentSales.put("instrumentSalesTheLeastOfDay", instrumentSalesTheLeastOfDay);
            instrumentSales.put("instrumentSalesTheLeastOfWeek", instrumentSalesTheLeastOfWeek);
            instrumentSales.put("instrumentSalesTheLeastOfMonth", instrumentSalesTheLeastOfMonth);

            return (ResponseEntity<?>) ResponseEntity.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
