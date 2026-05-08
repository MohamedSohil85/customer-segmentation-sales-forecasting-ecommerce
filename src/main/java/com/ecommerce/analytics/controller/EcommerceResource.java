package com.ecommerce.analytics.controller;

import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.service.EcommerceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EcommerceResource {
    private final EcommerceService ecService;

    public EcommerceResource(EcommerceService ecService) {
        this.ecService = ecService;
    }

    @PostMapping(value = "/readFile",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EcommerceDataset>> readFile() throws FileNotFoundException {
        return new ResponseEntity<>(ecService.readCSVFile(), HttpStatus.CREATED);
    }
    @PostMapping(value = "/customer",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EcommerceDataset>saveNewCustomer(@RequestBody EcommerceDataset customer) {
        return new ResponseEntity<>(ecService.saveNewData(customer), HttpStatus.CREATED);
    }


}
