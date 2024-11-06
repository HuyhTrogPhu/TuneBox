package org.example.ecommerceadmin.controller;

import org.example.library.dto.*;
import org.example.library.repository.BrandRepository;
import org.example.library.repository.CategoryInsRepository;
import org.example.library.service.*;
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

    @Autowired
    private CategoryInsRepository categoryInsRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

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
            revenue.put("revenueOfDay", revenueOfDay != null ? revenueOfDay : 0.0);
            revenue.put("revenueOfWeek", revenueOfWeek != null ? revenueOfWeek : 0.0);
            revenue.put("revenueOfMonth", revenueOfMonth != null ? revenueOfMonth : 0.0);
            revenue.put("revenueOfYear", revenueOfYear != null ? revenueOfYear : 0.0);

            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // statistical category
    @GetMapping("/category")
    public ResponseEntity<?> getStatisticsCategory(){
        try {
            List<CategorySalesDto> mostSalesDay = categoryInsRepository.getCategorySalesTheMostDay();
            List<CategorySalesDto> mostSalesWeek = categoryInsRepository.getCategorySalesTheMostWeek();
            List<CategorySalesDto> mostSalesMonth = categoryInsRepository.getCategorySalesTheMostMonth();
            List<CategorySalesDto> leastSalesDay = categoryInsRepository.getCategorySalesTheLeastDay();
            List<CategorySalesDto> leastSalesWeek = categoryInsRepository.getCategorySalesTheLeastWeek();
            List<CategorySalesDto> leastSalesMonth = categoryInsRepository.getCategorySalesTheLeastMonth();

            Map<String, Object> response = new HashMap<>();

            response.put("mostSoldToday", mostSalesDay);
            response.put("leastSoldToday", leastSalesDay);
            response.put("mostSoldThisWeek", mostSalesWeek);
            response.put("leastSoldThisWeek", leastSalesWeek);
            response.put("mostSoldThisMonth", mostSalesMonth);
            response.put("leastSoldThisMonth", leastSalesMonth);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get id and name category
    @GetMapping("/categoryForSta")
    public ResponseEntity<?> getNameAndIdCategory() {
        try {
            // Get id and name category
            List<StatisticalCategoryDto> statisticalCategories = categoryService.getIdsAndNamesCategory();
            return ResponseEntity.ok(statisticalCategories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get revenue category by category id
    @GetMapping("/revenue-category/{categoryId}")
    public ResponseEntity<?> getRevenueCategory(@PathVariable Long categoryId) {
        try {
            Double revenueCategoryOfDay = categoryService.getRevenueCategoryByDay(categoryId);
            Double revenueCategoryOfWeek = categoryService.getRevenueCategoryByWeek(categoryId);
            Double revenueCategoryOfMonth = categoryService.getRevenueCategoryByMonth(categoryId);
            Double revenueCategoryOfYear = categoryService.getRevenueCategoryByYear(categoryId);

            Map<String, Double> revenue = new HashMap<>();
            revenue.put("revenueOfDay", revenueCategoryOfDay!= null? revenueCategoryOfDay : 0.0);
            revenue.put("revenueOfWeek", revenueCategoryOfWeek!= null? revenueCategoryOfWeek : 0.0);
            revenue.put("revenueOfMonth", revenueCategoryOfMonth!= null? revenueCategoryOfMonth : 0.0);
            revenue.put("revenueOfYear", revenueCategoryOfYear!= null? revenueCategoryOfYear : 0.0);

            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // statistical brand
    @GetMapping("/brand")
    public ResponseEntity<?> getStatisticsBrand(){
        try {
            List<BrandSalesDto> mostSalesDay = brandRepository.getBrandSalesTheMostOdDay();
            List<BrandSalesDto> mostSalesWeek = brandRepository.getBrandSalesTheMostOfWeek();
            List<BrandSalesDto> mostSalesMonth = brandRepository.getBrandSalesTheMostOfMonth();
            List<BrandSalesDto> leastSalesDay = brandRepository.getBrandSalesTheLeastOdDay();
            List<BrandSalesDto> leastSalesWeek = brandRepository.getBrandSalesTheLeastOfWeek();
            List<BrandSalesDto> leastSalesMonth = brandRepository.getBrandSalesTheLeastOfMonth();

            Map<String, Object> response = new HashMap<>();

            response.put("mostSoldToday", mostSalesDay);
            response.put("leastSoldToday", leastSalesDay);
            response.put("mostSoldThisWeek", mostSalesWeek);
            response.put("leastSoldThisWeek", leastSalesWeek);
            response.put("mostSoldThisMonth", mostSalesMonth);
            response.put("leastSoldThisMonth", leastSalesMonth);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }

    }


    // get id and name brand
    @GetMapping("/brandForSta")
    public ResponseEntity<?> getNameAndIdBrand() {
        try {
            // Get id and name brand
            List<StatisticalBrandDto> statisticalBrands = brandService.getIdsAndNamesBrand();
            return ResponseEntity.ok(statisticalBrands);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // revenue brand by brand id
    @GetMapping("/revenue-brand/{brandId}")
    public ResponseEntity<?> getRevenueBrand(@PathVariable Long brandId) {
        try {
            Double revenueBrandOfDay = brandService.getRevenueByBrandIdOfDay(brandId);
            Double revenueBrandOfWeek = brandService.getRevenueByBrandIdOfWeek(brandId);
            Double revenueBrandOfMonth = brandService.getRevenueByBrandIdOfMonth(brandId);
            Double revenueBrandOfYear = brandService.getRevenueByBrandIdOfYear(brandId);

            Map<String, Double> revenue = new HashMap<>();
            revenue.put("revenueOfDay", revenueBrandOfDay!= null? revenueBrandOfDay : 0.0);
            revenue.put("revenueOfWeek", revenueBrandOfWeek!= null? revenueBrandOfWeek : 0.0);
            revenue.put("revenueOfMonth", revenueBrandOfMonth!= null? revenueBrandOfMonth : 0.0);
            revenue.put("revenueOfYear", revenueBrandOfYear!= null? revenueBrandOfYear : 0.0);

            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // map cho các trường hợp thống kê theo thời gian
    private Map<String, Object> createResponse(Double revenue, List<InstrumentAccordingTo> instruments, List<UserSell> users, String revenueKey) {
        Map<String, Object> response = new HashMap<>();
        response.put(revenueKey, revenue != null ? revenue : 0.0);
        response.put("listInstrument", instruments);
        response.put("userSells", users);
        return response;
    }

    // revenue by day
    @GetMapping("/revenue-according-date/{date}")
    public ResponseEntity<?> getRevenueDay(@PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByDay = orderService.revenueByDay(parsedDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByDay(parsedDate);
            List<UserSell> userSells = userService.getUserSellTheMostDay(parsedDate);

            return ResponseEntity.ok(createResponse(revenueByDay, list, userSells, "revenueByDay"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // revenue between days
    @GetMapping("/revenue-between-date/{startDate}/{endDate}")
    public ResponseEntity<?> getRevenueBetweenDate(@PathVariable String startDate, @PathVariable String endDate) {
        try {
            LocalDate parsedStartDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate parsedEndDate = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByDate = orderService.revenueBetweenDate(parsedStartDate, parsedEndDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentBetween(parsedStartDate, parsedEndDate);
            List<UserSell> userSells = userService.getUserSellBetweenDate(parsedStartDate, parsedEndDate);

            return ResponseEntity.ok(createResponse(revenueByDate, list, userSells, "revenueByDay"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // revenue by week
    @GetMapping("/revenue-by-week/{selectWeek}")
    public ResponseEntity<?> getRevenueByWeek(@PathVariable String selectWeek) {
        try {
            LocalDate parsedDate = LocalDate.parse(selectWeek, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Double revenueByWeek = orderService.revenueByWeek(parsedDate);
            List<InstrumentAccordingTo> list = instrumentService.getListInstrumentByWeek(parsedDate);
            List<UserSell> userSells = userService.getUserSellByWeek(parsedDate);

            return ResponseEntity.ok(createResponse(revenueByWeek, list, userSells, "revenueByWeek"));
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

            return ResponseEntity.ok(createResponse(revenueByWeek, list, userSells, "revenueByWeek"));
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

            return ResponseEntity.ok(createResponse(revenueByMonth, list, userSells, "revenueByMonth"));
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

            return ResponseEntity.ok(createResponse(revenueByMonth, list, userSells, "revenueByMonth"));
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

            return ResponseEntity.ok(createResponse(revenueByYear, list, userSells, "revenueByYear"));
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

            return ResponseEntity.ok(createResponse(revenueByYear, list, userSells, "revenueByYear"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by status unpaid
    @GetMapping("/order-unpaid")
    public ResponseEntity<?> getOrderUnpaid() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusUnpaid();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    // get list order by status paid
    @GetMapping("/order-paid")
    public ResponseEntity<?> getOrderPaid() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusPaid();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by status confirmed
    @GetMapping("/order-confirmed")
    public ResponseEntity<?> getOrderConfirmed() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusConfirmed();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by status delivered
    @GetMapping("/order-delivered")
    public ResponseEntity<?> getOrderDelivered() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusDelivered();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by status delivering
    @GetMapping("/order-delivering")
    public ResponseEntity<?> getOrderDelivering() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusDelivering();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by status canceled
    @GetMapping("/order-canceled")
    public ResponseEntity<?> getOrderCancelled() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByStatusCanceled();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by payment method cod
    @GetMapping("/order-cod")
    public ResponseEntity<?> getOrderCOD() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByPaymentMethodCOD();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by payment method vnpay
    @GetMapping("/order-vnpay")
    public ResponseEntity<?> getOrderVNPAY() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByPaymentMethodVNPAY();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by shipping method normal
    @GetMapping("/order-normal")
    public ResponseEntity<?> getOrderNormal() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByShippingMethodNormal();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    // get list order by shipping method fast
    @GetMapping("/order-fast")
    public ResponseEntity<?> getOrderFast() {
        try {
            List<StatisticalOrder> orders = orderService.getOrdersByShippingMethodFast();
            Map<String, Object> response = new HashMap<>();
            response.put("orders", orders);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
