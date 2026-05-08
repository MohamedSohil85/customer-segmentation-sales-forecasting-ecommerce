package com.ecommerce.analytics.service;

import com.ecommerce.analytics.entity.CustomerChurn;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.persistence.CustomerChurnRepository;
import com.ecommerce.analytics.persistence.CustomerIntelligenceRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomersChurnService {

    private final CustomerChurnRepository churnRepository;
    private final CustomerIntelligenceRepository intelligenceRepository;

    public CustomersChurnService(CustomerChurnRepository churnRepository, CustomerIntelligenceRepository intelligenceRepository) {
        this.churnRepository = churnRepository;
        this.intelligenceRepository = intelligenceRepository;
    }

    public List<CustomerChurn>saveCustomerChurn() {

        List<CustomerChurn>customerChurnList = new ArrayList<>();
        for (CustomerIntelligence customerIntelligence : intelligenceRepository.findAll()) {
           CustomerChurn customerChurn = new CustomerChurn();
           customerChurn.setFrequency(customerIntelligence.getTotalOrders());
           customerChurn.setPagesViewed(customerIntelligence.getAvgPagesViewed().intValue());
           double score=calculateChurnScore(customerIntelligence);
           String Label=mapScoreToLabel(score);
           customerChurn.setChurnLabel(Label);
           customerChurnList.add(customerChurn);

        }
        return churnRepository.saveAll(customerChurnList);
    }

    public double calculateChurnScore(CustomerIntelligence ci) {

        double score = 0;

        score += ci.getInactivityDays() * 0.3;
        score += (1.0 / (ci.getTotalOrders() + 1)) * 20;
        score += (1.0 - ci.getEngagementScore()) * 40;
        score += (1.0 - ci.getAvgRating().doubleValue() / 5.0) * 10;

        return score;
    }

    public String mapScoreToLabel(double score) {
        if (score > 50) return "HIGH_RISK";
        if (score > 25) return "MEDIUM_RISK";
        return "LOW_RISK";
    }
}
