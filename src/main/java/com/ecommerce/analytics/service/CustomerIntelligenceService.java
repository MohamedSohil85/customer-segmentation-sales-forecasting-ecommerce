package com.ecommerce.analytics.service;

import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.persistence.CustomerIntelligenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class CustomerIntelligenceService {

private final CustomerIntelligenceRepository customerIntelligenceRepository;

    public CustomerIntelligenceService(CustomerIntelligenceRepository customerIntelligenceRepository) {
        this.customerIntelligenceRepository = customerIntelligenceRepository;
    }

    public Page<CustomerIntelligence> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "customerId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return customerIntelligenceRepository.findAll(pageable);
    }
}
