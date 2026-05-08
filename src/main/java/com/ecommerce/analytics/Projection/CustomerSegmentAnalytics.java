package com.ecommerce.analytics.Projection;

import java.time.LocalDate;

public interface CustomerSegmentAnalytics {

        String getCustomerID();
        String getSegmentName();
        Integer getSegmentScore();
        String getChurnRiskLevel();
        String getEngagementLevel();
        String getCustomerValueLabel();
        LocalDate getSegmentationDate();

}
