package com.ecommerce.analytics.controller;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.Projection.CustomerSegmentAnalytics;
import com.ecommerce.analytics.entity.CustomerSegmentation;
import com.ecommerce.analytics.service.CustomerSegmentationService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CustomersSegmentationResource {

    private final CustomerSegmentationService customerSegmentationService;

    public CustomersSegmentationResource(CustomerSegmentationService customerSegmentationService) {
        this.customerSegmentationService = customerSegmentationService;
    }

    @GetMapping(value = "/segmentations",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentAnalytics>> getSegmentations(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size) {
        return new ResponseEntity<>(customerSegmentationService.getAllCustomerSegmentations(page, size), HttpStatus.OK);
    }
    @PostMapping(value = "/segmentation",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> addSegmentation() {
        return new ResponseEntity<>(customerSegmentationService.saveAllCustomerSegmentations(),HttpStatus.CREATED);
    }
    @GetMapping(value = "/{customerId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerSegmentation> getSegmentation(@PathVariable String  customerId) {
        return new ResponseEntity<>(customerSegmentationService.getCustomerSegmentation(customerId),HttpStatus.OK);
    }
    @GetMapping(value = "/segmentations/{segmentName}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> getSegmentationByName(@PathVariable String segmentName) {
        return new ResponseEntity<>(customerSegmentationService.findBySegmentName(segmentName),HttpStatus.OK);
    }
    @GetMapping(value = "/segmentName",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<CustomerSegmentation>> getSegmentationBySegmentName(@RequestParam String segmentName, @RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size) {
        return new ResponseEntity<>(customerSegmentationService.findBySegmentName(segmentName, page, size),HttpStatus.OK);
    }
@GetMapping(value = "/segmentScore",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> getSegmentScoreGreaterThan(@RequestParam Integer segmentScore) {
        return new ResponseEntity<>(customerSegmentationService.findBySegmentScoreGreaterThan(segmentScore),HttpStatus.OK);
}
@GetMapping(value = "/churnRiskLevel",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> churnRiskLevel(@RequestParam String level) {
        return new ResponseEntity<>(customerSegmentationService.findByChurnRisk(level),HttpStatus.OK);
}
@GetMapping(value = "/churn-Risk-High",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> churnRiskHigh() {
        return new ResponseEntity<>(customerSegmentationService.findHighRiskCustomers(),HttpStatus.OK);
}
@GetMapping(value = "/customer-value",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> getCustomerValue(@RequestParam String customerValue) {
        return new ResponseEntity<>(customerSegmentationService.findByCustomerValueLabel(customerValue),HttpStatus.OK);
}
@GetMapping(value = "/top10segmentScore",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> getTop10SegmentScore(@RequestParam int page, @RequestParam int size) {
        return new ResponseEntity<>(customerSegmentationService.findTop10BySegmentScore(page, size),HttpStatus.OK);
}
@GetMapping(value = "/customerHighValueHighChurn",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerSegmentation>> getCustomerHighValueHighChurn() {
        return new ResponseEntity<>(customerSegmentationService.findHighValueHighRisk(),HttpStatus.OK);
}
@GetMapping(value = "/high-engagement-low-risk",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity <List<CustomerSegmentation>> getHighEngagementLowRisk() {
     return new ResponseEntity<>(customerSegmentationService.findHighEngagementLowRisk(),HttpStatus.OK);
}
@GetMapping(value = "/countBySegmentName",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Integer>> getCountBySegmentName(@RequestParam String segmentName) {
        Map<String,Integer> result = new HashMap<>();
        result=customerSegmentationService.countBySegmentName(segmentName);
        return new ResponseEntity<>(result,HttpStatus.OK);
}
@GetMapping(value = "/churn-risk-levels/count",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Integer>> getCountByChurnLevel() {
        Map<String,Integer> result = new HashMap<>();
        result=customerSegmentationService.countByChurnRiskLevel();
        return new ResponseEntity<>(result,HttpStatus.OK);
}
@GetMapping(value = "/customer-values/count",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Integer>> getCountByCustomerValue() {
        Map<String,Integer> result = new HashMap<>();
        result=customerSegmentationService.countByCustomerValue();
        return new ResponseEntity<>(result,HttpStatus.OK);
}
}
