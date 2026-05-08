package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.Projection.CustomerSegmentAnalytics;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.entity.CustomerSegmentation;
import com.ecommerce.analytics.persistence.CustomerIntelligenceRepository;
import com.ecommerce.analytics.persistence.CustomerSegmentationRepository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CustomerSegmentationService {

    private final CustomerSegmentationRepository segmentationRepository;
    private final CustomerIntelligenceRepository inteligenceRepository;

    public CustomerSegmentationService(CustomerSegmentationRepository segmentationRepository, CustomerIntelligenceRepository inteligenceRepository) {
        this.segmentationRepository = segmentationRepository;
        this.inteligenceRepository = inteligenceRepository;
    }

    public List<CustomerSegmentAnalytics> getAllCustomerSegmentations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC);

        return segmentationRepository.findAllCustomerSegmentations(pageable);
    }

    @Transactional
    public List<CustomerSegmentation> saveAllCustomerSegmentations() {

        List<CustomerIntelligence> customerIntelligences = inteligenceRepository.findAll();
        List<CustomerSegmentation> customerSegmentationList = new ArrayList<>();

        for (CustomerIntelligence customerIntelligence : customerIntelligences) {
            CustomerSegmentation customerSegmentation = new CustomerSegmentation();
            customerSegmentation.setCustomerID(customerIntelligence.getCustomerId());
            customerSegmentation.setSegmentName(calculateSegmentName(customerIntelligence));
            customerSegmentation.setSegmentScore(calculateSegmentScore(customerIntelligence));
            customerSegmentation.setChurnRiskLevel(calculateChurnRisk(customerIntelligence));
            customerSegmentation.setEngagementLevel(calculateEngagementLevel(customerIntelligence));
            customerSegmentation.setCustomerValueLabel(calculateValueTier(customerIntelligence));
            customerSegmentation.setSegmentationDate(LocalDate.now());
            customerSegmentationList.add(customerSegmentation);
        }
        segmentationRepository.deleteCustomerSegmentation();
        segmentationRepository.saveAll(customerSegmentationList);
        return customerSegmentationList;
    }

    private String calculateSegmentName(CustomerIntelligence ci) {

        int r = ci.getRScore();
        int f = ci.getFScore();
        int m = ci.getMScore();

        double engagement = ci.getEngagementScore();
        int recency = ci.getRecency();
        int tenure = ci.getCustomerTenureDays();
        BigDecimal rating = ci.getAvgRating();

        if ((recency >= 60 && engagement < 10 && ci.getTotalOrders() >= 3)) {
            return "At_Risk";
        } else if (r >= 3 && f >= 3 && m >= 3 && engagement >= 20 && recency <= 30) {
            return "Loyal";
        } else if (recency > 90 && engagement < 10 && f <= 2) {
            return "Churned";
        } else if (tenure < 30 && ci.getTotalOrders() == 1) {
            return "New Customer";
        } else if (rating != null && rating.compareTo(new BigDecimal("3.0")) < 0) {
            return "Negative Experience";
        } else {
            return "General Customer";
        }
    }

    private int calculateSegmentScore(CustomerIntelligence c) {
        return c.getRScore() + c.getFScore() + c.getMScore();
    }

    private String calculateChurnRisk(CustomerIntelligence c) {
        int recency = c.getRecency();
        double engagement = c.getEngagementScore();

        if (recency > 90 && engagement < 10) {
            return "High";
        } else if (recency >= 60 && engagement < 20) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    public String calculateEngagementLevel(CustomerIntelligence c) {
        double engagement = c.getEngagementScore();
        if (engagement >= 70) return "High";
        else if (engagement >= 40) return "Medium";
        else return "Low";
    }

    public String calculateValueTier(CustomerIntelligence c) {
        int rfmTotal = c.getRScore() + c.getFScore() + c.getMScore();
        if (rfmTotal >= 8) return "High";
        else if (rfmTotal >= 5) return "Medium";
        else return "Low";
    }


    // Retrieves all segmentation records for a given customer ID.
    // Use case: customer history / timeline of segmentation.
    // Returns: List<CustomerSegmentationView>
    // Params: String customer_id
    // Method: findByCustomerID(String)
    public CustomerSegmentation getCustomerSegmentation(String customerID) {
        return segmentationRepository.findAll().stream()
                .filter(customerSegmentation1 -> customerSegmentation1.getCustomerID().equals(customerID))
                .findAny().orElse(null);
    }

    // =========================================
    //  SEGMENT NAME
    // =========================================

    // Retrieves customers belonging to a specific segment.
    // Returns: List<CustomerSegmentationViewView>
    // Params: String segmentName
    // Method: findBySegmentName(String)
    public List<CustomerSegmentation> findBySegmentName(String segmentName) {
        List<CustomerSegmentation> segmentations = segmentationRepository
                .findAll().stream().filter(customerSegmentation -> customerSegmentation.getSegmentName().equalsIgnoreCase(segmentName))
                .toList();
        return segmentations;
    }

    // Same as above but paginated.
    // Returns: Page<CustomerSegmentation>
    // Params: String segmentName, Pageable pageable
    // Method: findBySegmentName(String, Pageable)
    public Page<CustomerSegmentation> findBySegmentName(String segmentName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC);
        return segmentationRepository.findBySegmentNameIgnoreCase(pageRequest, segmentName);

    }

    // =========================================
    //  SEGMENT SCORE
    // =========================================

    // Retrieves customers with segment score greater than a threshold.
    // Use case: high-quality or priority customers.
    // Returns: List<CustomerSegmentation>
    // Method: findBySegmentScoreGreaterThan(Integer)
    public List<CustomerSegmentation> findBySegmentScoreGreaterThan(Integer score) {
        List<CustomerSegmentation> customerSegmentations = segmentationRepository.findCustomerSegmentationBySegmentScoreGreaterThan(score).stream().toList();
        return customerSegmentations;
    }

    // Retrieves top 10 customers ranked by segment score (descending).
    // Returns: List<CustomerSegmentationView>
    // Method: findTop10ByOrderBySegmentScoreDesc()
    public List<CustomerSegmentation> findTop10BySegmentScore(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC,"segmentName");
        List<CustomerSegmentation> customerSegmentations = segmentationRepository.findTopByOrderBySegmentScoreDesc(pageable).stream().limit(10).toList();
        return customerSegmentations;
    }

    // =========================================
    //  CHURN RISK
    // =========================================

    // Retrieves customers by churn risk level.
    // Returns: List<CustomerSegmentation>
    // Method: findByChurnRiskLevel(String)
    public List<CustomerSegmentation> findByChurnRisk(String churnRiskLevel) {

        return switch (churnRiskLevel.toLowerCase()) {
            case "high" -> segmentationRepository.findCustomerSegmentationByChurnRiskLevel("High");
            case "medium" -> segmentationRepository.findCustomerSegmentationByChurnRiskLevel("Medium");
            case "low" -> segmentationRepository.findCustomerSegmentationByChurnRiskLevel("Low");
            default -> throw new IllegalStateException("Unexpected value: " + churnRiskLevel.toLowerCase());
        };

    }


    // Retrieves high-risk customers explicitly.
    // Custom query: WHERE churnRiskLevel = 'HIGH'
    // Returns: List<CustomerSegmentation>
    // Method: findHighRiskCustomers()

    public List<CustomerSegmentation> findHighRiskCustomers() {
        List<CustomerSegmentation> customerSegmentations = segmentationRepository.findAll().stream()
                .filter(customerSegmentation -> customerSegmentation.getSegmentName().equalsIgnoreCase("High"))
                .toList();
        return customerSegmentations;
    }
    // =========================================
    //  ENGAGEMENT
    // =========================================

    // Retrieves customers by engagement level.
    // Returns: List<CustomerSegmentation>
    // Method: findByEngagementLevel(String)

    public List<CustomerSegmentation> findByEngagementLevel(String engagementLevel, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC);
    return  switch (engagementLevel.toLowerCase()) {
            case "high" -> segmentationRepository.findCustomerSegmentationByEngagementLevel(pageRequest, "High");
            case "medium" -> segmentationRepository.findCustomerSegmentationByEngagementLevel(pageRequest, "Medium");
            case "low" -> segmentationRepository.findCustomerSegmentationByEngagementLevel(pageRequest, "Low");
        default -> throw new IllegalStateException("Unexpected value: " + engagementLevel.toLowerCase());
    };

    }




        // =========================================
        //  CUSTOMER VALUE
        // =========================================

        // Retrieves customers by value label (e.g., High, Medium, Low).
        // Returns: List<CustomerSegmentation>
        // Method: findByCustomerValueLabel(String)
        public List<CustomerSegmentation>findByCustomerValueLabel(String label){
        return switch (label.toLowerCase()){
            case "high" -> segmentationRepository.findCustomerSegmentationByCustomerValueLabel("High");
            case "medium" -> segmentationRepository.findCustomerSegmentationByCustomerValueLabel("Medium");
            case "low" -> segmentationRepository.findCustomerSegmentationByCustomerValueLabel("Low");

            default -> throw new IllegalStateException("Unexpected value: " + label.toLowerCase());
        };
        }



        // =========================================
        //  COMBINED SEGMENTATION (BUSINESS-CRITICAL)
        // =========================================

        // High value + high churn risk → Critical retention segment.
        // Returns: List<CustomerSegmentation>
        // Method: findHighValueHighRisk()
        public List<CustomerSegmentation>findHighValueHighRisk(){
            Predicate<CustomerSegmentation> isHighValueHighRisk = customerSegmentation ->
                    "High".equalsIgnoreCase(customerSegmentation.getCustomerValueLabel()) &&
                            "High".equalsIgnoreCase(customerSegmentation.getChurnRiskLevel());

            return segmentationRepository.findAll()
                .stream()
                .filter(isHighValueHighRisk)
                .toList();

        }
        // High engagement + low churn risk → Loyal customers.
        // Returns: List<CustomerSegmentation>
        // Method: findLoyalCustomers()

        public List<CustomerSegmentation>findHighEngagementLowRisk(){

        Predicate<CustomerSegmentation>customerSegmentationPredicate=customerSegmentation->
               "High".equalsIgnoreCase(customerSegmentation.getEngagementLevel()) &&
                "High".equalsIgnoreCase(customerSegmentation.getChurnRiskLevel());
            return segmentationRepository
                    .findAll()
                    .stream()
                    .filter(customerSegmentationPredicate).toList();

        }
        // =========================================
        // DATE FILTERING
        // =========================================

        // Retrieves segmentation records for a specific date.
        // Returns: List<CustomerSegmentation>
        // Method: findBySegmentationDate(LocalDate)
        public Map<LocalDate,List<CustomerSegmentation>>findBySegmentationDate(LocalDate segmentationDate) {
        List<CustomerSegmentation>customerSegmentations = segmentationRepository.findAll().stream()
                .filter(customerSegmentation -> customerSegmentation.getSegmentationDate().equals(segmentationDate))
                .toList();
        return Map.of(segmentationDate, customerSegmentations);
        }
        // Retrieves segmentation records within a date range.
        // Returns: List<CustomerSegmentation>
        // Method: findBySegmentationDateBetween(LocalDate, LocalDate)
        public List<CustomerSegmentation>findBySegmentationDateBetween(LocalDate startDate, LocalDate endDate) {

            return segmentationRepository.findCustomerSegmentationBySegmentationDateBetween(startDate, endDate);
        }


        // =========================================
        //  DASHBOARD / ANALYTICS
        // =========================================

        // Aggregates count of customers per segment.
        // Returns: List<Object[]> → [segmentName, count]
        // Method: countBySegment()
        public Map<String,Integer>countBySegmentName(String segmentName){
        Map<String,Integer>countBySegmentName = new HashMap<>();
        Integer count = Math.toIntExact(segmentationRepository.findAll()
                .stream().filter(customerSegmentation -> customerSegmentation.getSegmentName().equalsIgnoreCase(segmentName)).count());
        countBySegmentName.put(segmentName,count);
        return countBySegmentName;

        }

        // Aggregates count by churn risk level.
        // Returns: List<Object[]> → [churnRiskLevel, count]
        // Method: countByChurnRisk()
        public Map<String,Integer>countByChurnRiskLevel(){
        Map<String,Integer>customerRiskValue=new HashMap<>();
        int high = 0,medium = 0,low = 0;
        List<CustomerSegmentation>customerSegmentations=segmentationRepository.findAll();
        for(CustomerSegmentation customerSegmentation:customerSegmentations){

            switch(customerSegmentation.getChurnRiskLevel()){
                case "High" -> high++;
                case "Medium" -> medium++;
                case "Low" -> low++;
            }
        }
        customerRiskValue.put("Customers with High Risk :",high);
        customerRiskValue.put("Customers with Medium Risk :",medium);
        customerRiskValue.put("Customers with Low Risk :",low);

        return customerRiskValue;
        }


        // Aggregates count by customer value label.
        // Returns: List<Object[]> → [customerValueLabel, count]
        // Method: countByCustomerValue()
       public Map<String,Integer>countByCustomerValue(){
        int high = 0,medium = 0,low = 0;
        Map<String,Integer>countByCustomerValue=new HashMap<>();
        List<CustomerSegmentation>customerSegmentations=segmentationRepository.findAll();
        for(CustomerSegmentation customerSegmentation:customerSegmentations){
            switch(customerSegmentation.getCustomerValueLabel()){
                case "High" -> high++;
                case "Medium" -> medium++;
                case "Low" -> low++;
            }
        }
        countByCustomerValue.put("Customers with High Value ",high);
        countByCustomerValue.put("Customers with Medium Value ",medium);
        countByCustomerValue.put("Customers with Low Value ",low);

        return countByCustomerValue;
        }


    }
