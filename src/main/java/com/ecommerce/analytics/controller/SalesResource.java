package com.ecommerce.analytics.controller;

import com.ecommerce.analytics.Projection.PeakSalesView;
import com.ecommerce.analytics.Projection.SalesAnalyticsView;
import com.ecommerce.analytics.Projection.SeasonalSalesAnalyticsView;
import com.ecommerce.analytics.entity.Sales;
import com.ecommerce.analytics.service.SalesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SalesResource {
    private final SalesService salesService;

    public SalesResource(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping(value = "/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalesAnalyticsView>> getSales(@RequestParam String category, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(salesService.getAllSales(category, startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(value = "/sales-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalesAnalyticsView>> getSalesCategory(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(salesService.findTopCategories(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(value = "/sales-trend", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SalesAnalyticsView>> getSalesTrend(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {

        return ResponseEntity.ok(salesService.findTopSalesByDate(startDate, endDate));
    }

    @GetMapping(value = "/sales-peak", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PeakSalesView>> getSalesPeak(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(salesService.findPeakSalesDays(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(value = "/sales-seasonal", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SeasonalSalesAnalyticsView>> findSeasonalSales(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(salesService.findSeasonalSalesDays(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping(value = "/peak-months", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<YearMonth>> findPeakSalesMonths(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return new ResponseEntity<>(salesService.findPeakSalesMonths(startDate, endDate), HttpStatus.OK);

    }

}
