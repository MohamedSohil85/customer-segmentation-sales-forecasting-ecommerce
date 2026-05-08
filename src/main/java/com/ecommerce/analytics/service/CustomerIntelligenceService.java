package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.persistence.CustomerIntelligenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerIntelligenceService {

private final CustomerIntelligenceRepository customerIntelligenceRepository;

    public CustomerIntelligenceService(CustomerIntelligenceRepository customerIntelligenceRepository) {
        this.customerIntelligenceRepository = customerIntelligenceRepository;
    }
    // Retrieves all customers with pagination support.
    // Equivalent to: SELECT * FROM customer_intelligence
    // Use case: general listing, for Example :Admin dashboards.
    // Returns: Page<CustomerIntelligence>
    // Params: Pageable pageable
    // Method: getAllCustomers(Pageable pageable)
    public List<CustomerAnalyticsView> getAllCustomers(
             int page,
             int size) {
        Pageable pageable = PageRequest.of(page, size);

        return customerIntelligenceRepository.findAllCustomerIntelligence(pageable);
    }

    // Retrieves customers filtered by returning status.
    // Equivalent to: WHERE is_returning_customer = ?
    // Use case: segmentation between new vs returning users.
    // Returns: Page<CustomerIntelligence>
    // Params: Boolean isReturningCustomer, Pageable pageable
    // Method: findByIsReturningCustomer(Boolean, Pageable)
public List<CustomerAnalyticsView> getCustomersByReturningStatus(boolean returningStatus ,int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return customerIntelligenceRepository.findAllCustomerIntelligence(pageable).stream().filter(CustomerAnalyticsView -> CustomerAnalyticsView.getIsReturningCustomer().equals(returningStatus)).toList();
}

    // Retrieves "active" customers based on recency threshold.
    // Equivalent to: WHERE recency < ?
    // Interpretation: customers who interacted recently (lower recency = more recent activity).
    // Returns: Page<CustomerIntelligence>
    // Params: Integer days, Pageable pageable
    // Method: findByRecencyLessThan(Integer, Pageable)
public List<CustomerAnalyticsView> findByRecencyLessThan(Integer recency, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
    return customerIntelligenceRepository.findAllCustomerIntelligence(pageable)
                .stream().filter(CustomerAnalyticsView -> CustomerAnalyticsView.getRecency() < recency).toList();
}


    // ============================
    // Fast Growth Customers
    // ============================

    // Retrieves customers who are growing rapidly in a short time.
    // Equivalent to: WHERE customerTenureDays < ? AND totalOrders > ?
   // Use case: identify new customers with strong early engagement (high potential / fast adopters).
   // Returns: List<CustomerIntelligence>
   // Params: int days, int orders
   // Method: findFastGrowingCustomers(int, int)
   public List<CustomerAnalyticsView>findFastGrowingCustomers(int days,int orders){
        return customerIntelligenceRepository.findFastGrowingCustomers(days,orders);
   }

    // Retrieves top-tier customers based on combined behavioral and value signals.
    // Equivalent to: WHERE engagementScore > ? AND monetary > ? AND inactivityDays < ?
    // Use case: identify the best customers (high engagement + high value + low churn risk) for VIP targeting, retention, and upselling.
    // Returns: List<CustomerIntelligence>
    // Params: double engagement, BigDecimal monetary, LocalDate inactivity
    // Method: findBestCustomers(double, BigDecimal, LocalDate)
    public List<CustomerAnalyticsView>findBestCustomers(double engagement, BigDecimal monetary, LocalDate inactivity){
        return customerIntelligenceRepository.findBestCustomers(engagement,monetary,inactivity);
    }

    // Retrieves customers at risk of churn based on behavioral signals.
    // Equivalent to: WHERE inactivityDays > ? AND engagementScore < ? AND recency > ?
    // Use case: identify disengaging customers (low activity, low engagement, long time since last purchase) for retention campaigns.
    // Returns: List<CustomerIntelligence>
    // Params: int days, double engagement, int recency
    // Method: findAtRiskCustomers(int, double, int)
    public List<CustomerAnalyticsView>findAtRiskCustomers(int engagement, int recency, LocalDate inactivity){
        return customerIntelligenceRepository.findAtRiskCustomers(inactivity,engagement,recency);
    }



    // ============================
    // Monetary-Based Segmentation
    // ============================

    // Retrieves customers with monetary value greater than a threshold.
    // Equivalent to: WHERE monetary > ?
    // Use case: high spenders / VIP identification.
    // Returns: Page<CustomerIntelligence>
    // Params: BigDecimal monetary, Pageable pageable
    // Method: findByMonetaryGreaterThan(BigDecimal, Pageable)
    public List<CustomerAnalyticsView> findByMonetaryGreaterThan(double monetary, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    return customerIntelligenceRepository.findAllCustomerIntelligence(pageable).stream().filter(CustomerAnalyticsView -> CustomerAnalyticsView.getMonetary()>monetary).toList();
    }






}
