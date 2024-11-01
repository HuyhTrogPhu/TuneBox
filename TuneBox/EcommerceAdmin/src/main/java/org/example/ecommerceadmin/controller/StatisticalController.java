package org.example.ecommerceadmin.controller;

import org.example.library.dto.*;
import org.example.library.service.InstrumentService;
import org.example.library.service.OrderService;
import org.example.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    // get instrument name and id
    @GetMapping("/instrumentForSta")
    public ResponseEntity<?> getNameAndIdInstrument() {
        try {
            // Get id and name instrument
            List<StatisticalInstrumentDto> statisticalInstruments = instrumentService.getIdAndNameInstrument();
            return ResponseEntity.ok(statisticalInstruments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // statistical instrument
    @GetMapping("/instrument")
    public ResponseEntity<?> getStatisticalInstrument() {
        try {


            List<InstrumentSalesDto> mostSoldToday = instrumentService.instrumentSalesTheMostOfDay();
            List<InstrumentSalesDto> leastSoldToday = instrumentService.instrumentSalesTheLeastOfDay();
            List<InstrumentSalesDto> mostSoldThisWeek = instrumentService.instrumentSalesTheMostOfWeek();
            List<InstrumentSalesDto> leastSoldThisWeek = instrumentService.instrumentSalesTheLeastOfWeek();
            List<InstrumentSalesDto> mostSoldThisMonth = instrumentService.instrumentSalesTheMostOfMonth();
            List<InstrumentSalesDto> leastSoldThisMonth = instrumentService.instrumentSalesTheLeastOfMonth();

            Map<String, Object> response = new HashMap<>();


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


    // get revenue instrument by instrument id
    @GetMapping("/revenue-instrument/{instrumentId}")
    public ResponseEntity<?> getRevenueInstrument(@PathVariable Long instrumentId) {
        try {
            Double revenueOfDay = instrumentService.getRevenueInstrumentOfDay(instrumentId);
            Double revenueOfWeek = instrumentService.getRevenueInstrumentOfWeek(instrumentId);
            Double revenueOfMonth = instrumentService.getRevenueInstrumentOfMonth(instrumentId);
            Double revenueOfYear = instrumentService.getRevenueInstrumentOfYear(instrumentId);

            Map<String, Double> revenue = new HashMap<>();
            revenue.put("revenueOfDay", revenueOfDay!= null? revenueOfDay : 0.0);
            revenue.put("revenueOfWeek", revenueOfWeek!= null? revenueOfWeek : 0.0);
            revenue.put("revenueOfMonth", revenueOfMonth!= null? revenueOfMonth : 0.0);
            revenue.put("revenueOfYear", revenueOfYear!= null? revenueOfYear : 0.0);

            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // revenue by day
    @GetMapping("/revenue-according-date/{date}")
    public ResponseEntity<?> getRevenueDay(@PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByDay = orderService.revenueByDay(parsedDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByDay(parsedDate);
            List<UserSell> userSells = userService.getUserSellTheMostDay(parsedDate);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByDay", revenueByDay != null ? revenueByDay : 0.0);
            response.put("listInstrumentByDay", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // revenue between days
    @GetMapping("/revenue-between-date/{startDate}/{endDate}")
    public ResponseEntity<?> getRevenueBetweenDate(
            @PathVariable String startDate,
            @PathVariable String endDate) {
        try {
            LocalDate parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByDate = orderService.revenueBetweenDate(parsedStartDate, parsedEndDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentBetween(parsedStartDate, parsedEndDate);
            List<UserSell> userSells = userService.getUserSellBetweenDate(parsedStartDate, parsedEndDate);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByDate", revenueByDate != null ? revenueByDate : 0.0);
            response.put("listInstrumentByDate", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // revenue by week
    @GetMapping("/revenue-by-week/{date}")
    public ResponseEntity<?> getRevenueByWeek(@PathVariable String date) {
        try {
            // Log nhận date từ request
            System.out.println("Received date: " + date);

            // Thử phân tích cú pháp ngày
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Parsed date: " + parsedDate);

            // Truy xuất doanh thu theo tuần
            Double revenueByWeek = orderService.revenueByWeek(parsedDate);
            System.out.println("Revenue by week: " + revenueByWeek);

            // Truy xuất danh sách instrument và user bán trong tuần
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByWeek(parsedDate);
            List<UserSell> userSells = userService.getUserSellByWeek(parsedDate);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByWeek", revenueByWeek != null ? revenueByWeek : 0.0);
            response.put("listInstrumentByWeek", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid date format or internal error.");
        }
    }



    // revenue between weeks
    @GetMapping("/revenue-between-weeks/{startDate}/{endDate}")
    public ResponseEntity<?> getRevenueBetweenWeeks(@PathVariable String startDate, @PathVariable String endDate) {
        try {
            LocalDate parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByWeek = orderService.revenueBetweenWeeks(parsedStartDate, parsedEndDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentBetweenWeek(parsedStartDate, parsedEndDate);
            List<UserSell> userSells = userService.getUserSellBetweenWeek(parsedStartDate, parsedEndDate);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByWeek", revenueByWeek!= null? revenueByWeek : 0.0);
            response.put("listInstrumentByWeek", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // revenue by month
    @GetMapping("/revenue-by-month/{year}/{month}")
    public ResponseEntity<?> getRevenueByMonth(@PathVariable int year, @PathVariable int month) {
        try {

            Double revenueByMonth = orderService.revenueByMonth(year, month);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByMonth(year, month);
            List<UserSell> userSells = userService.getUserSellByMonth(year, month);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByMonth", revenueByMonth!= null? revenueByMonth : 0.0);
            response.put("listInstrumentByMonth", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // revenue between months
    @GetMapping("/revenue-between-months/{year}/{startMonth}/{endMonth}")
    public ResponseEntity<?> getRevenueBetweenMonths(@PathVariable int year, @PathVariable int startMonth, @PathVariable int endMonth) {
        try {

            Double revenueByMonth = orderService.revenueBetweenMonths(year, startMonth, endMonth);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentBetweenMonth(year, startMonth, endMonth);
            List<UserSell> userSells = userService.getUserSellBetweenMonth(year, startMonth, endMonth);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByMonth", revenueByMonth!= null? revenueByMonth : 0.0);
            response.put("listInstrumentByMonth", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // revenue by year
    @GetMapping("/revenue-by-year/{year}")
    public ResponseEntity<?> getRevenueByYear(@PathVariable int year) {
        try {

            Double revenueByYear = orderService.revenueByYear(year);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByYear(year);
            List<UserSell> userSells = userService.getUserSellByYear(year);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByYear", revenueByYear!= null? revenueByYear : 0.0);
            response.put("listInstrumentByYear", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // revenue between years
    @GetMapping("/revenue-between-years/{startYear}/{endYear}")
    public ResponseEntity<?> getRevenueBetweenYears(@PathVariable int startYear, @PathVariable int endYear) {
        try {

            Double revenueByYear = orderService.revenueBetweenYears(startYear, endYear);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentBetweenYear(startYear, endYear);
            List<UserSell> userSells = userService.getUserSellBetweenYear(startYear, endYear);

            Map<String, Object> response = new HashMap<>();
            response.put("revenueByYear", revenueByYear != null ? revenueByYear : 0.0);
            response.put("listInstrumentByYear", list);
            response.put("userSells", userSells);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }



}
