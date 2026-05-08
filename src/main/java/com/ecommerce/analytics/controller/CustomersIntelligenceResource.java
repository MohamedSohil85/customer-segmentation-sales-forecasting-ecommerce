package com.ecommerce.analytics.controller;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.service.CustomerIntelligenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomersIntelligenceResource {

    private final CustomerIntelligenceService customerIntelligenceService;

    public CustomersIntelligenceResource(CustomerIntelligenceService customerIntelligenceService) {
        this.customerIntelligenceService = customerIntelligenceService;
    }

    @GetMapping(value = "/customers-intelligence", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>> findAllCustomersIntelligence(@RequestParam(defaultValue = "0") int page,
                                                                                             @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(customerIntelligenceService.getAllCustomers(page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/customers-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>> findCustomersByStatus(@RequestParam(defaultValue = "false") boolean status,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {

        return new ResponseEntity<>(customerIntelligenceService.getCustomersByReturningStatus(status, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/active-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>>findActiveCustomers(@RequestParam("recency") Integer recency,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(customerIntelligenceService.findByRecencyLessThan(recency, page, size), HttpStatus.OK);
    }

    @GetMapping(value = "/vip-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>>findHighSpenderCustomers(@RequestParam("monetary") Integer monetary,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size){
        return new ResponseEntity<>(customerIntelligenceService.findByMonetaryGreaterThan(monetary, page, size), HttpStatus.OK);
    }
    @GetMapping(value = "/customers/fast-growth",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>>findFastGrowingCustomers(@RequestParam int days,@RequestParam int orders){
        return new ResponseEntity<>(customerIntelligenceService.findFastGrowingCustomers(days, orders),HttpStatus.OK);
    }
@GetMapping(value = "/best-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>>findBestCustomers(@RequestParam double engagement,@RequestParam BigDecimal monetary,@RequestParam LocalDate inactivity){
        return new ResponseEntity<>(customerIntelligenceService.findBestCustomers(engagement, monetary, inactivity), HttpStatus.OK);
}
@GetMapping(value = "/atRist-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerAnalyticsView>>findAtRistCustomers( @RequestParam int engagementScore,@RequestParam int recency,@RequestParam LocalDate inactivity){
        return new ResponseEntity<>(customerIntelligenceService.findAtRiskCustomers(engagementScore, recency, inactivity), HttpStatus.OK);
}
}