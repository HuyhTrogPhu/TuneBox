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

    // statistical revenue before currently
    @GetMapping("/revenue-before-currently")
    public ResponseEntity<?> getRevenueBeforeCurrently() {
        try {
            Double revenueBeforeOfDay = orderService.revenueBeforeOfDay();
            Double revenueBeforeOfWeek = orderService.revenueBeforeOfWeek();
            Double revenueBeforeOfMonth = orderService.revenueBeforeOfMonth();
            Double revenueBeforeOfYear = orderService.revenueBeforeOfYear();

            // Tạo object hoặc Map chứa kết quả trả về
            Map<String, Double> revenue = new HashMap<>();
            revenue.put("revenueBeforeOfDay", revenueBeforeOfDay != null ? revenueBeforeOfDay : 0.0);
            revenue.put("revenueBeforeOfWeek", revenueBeforeOfWeek != null ? revenueBeforeOfWeek : 0.0);
            revenue.put("revenueBeforeOfMonth", revenueBeforeOfMonth != null ? revenueBeforeOfMonth : 0.0);
            revenue.put("revenueAfterOfYear", revenueBeforeOfYear != null ? revenueBeforeOfYear : 0.0);

            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/instrument")
    public ResponseEntity<?> getStatisticalInstrument() {
        try {
            // Get id and name instrument
            List<StatisticalInstrumentDto> statisticalInstruments = instrumentService.getIdAndNameInstrument();

            List<InstrumentSalesDto> mostSoldToday = instrumentService.instrumentSalesTheMostOfDay();
            List<InstrumentSalesDto> leastSoldToday = instrumentService.instrumentSalesTheLeastOfDay();
            List<InstrumentSalesDto> mostSoldThisWeek = instrumentService.instrumentSalesTheMostOfWeek();
            List<InstrumentSalesDto> leastSoldThisWeek = instrumentService.instrumentSalesTheLeastOfWeek();
            List<InstrumentSalesDto> mostSoldThisMonth = instrumentService.instrumentSalesTheMostOfMonth();
            List<InstrumentSalesDto> leastSoldThisMonth = instrumentService.instrumentSalesTheLeastOfMonth();

            Map<String, Object> response = new HashMap<>();
            response.put("statisticalInstruments", statisticalInstruments);

            response.put("mostSoldToday", mostSoldToday);
            response.put("leastSoldToday", leastSoldToday);
            response.put("mostSoldThisWeek", mostSoldThisWeek);
            response.put("leastSoldThisWeek", leastSoldThisWeek);
            response.put("mostSoldThisMonth", mostSoldThisMonth);
            response.put("leastSoldThisMonth", leastSoldThisMonth);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error retrieving data: " + e.getMessage());
        }
    }


}
