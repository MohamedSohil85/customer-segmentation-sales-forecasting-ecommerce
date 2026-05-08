package com.ecommerce.analytics.Projection;

import java.time.LocalDate;
import java.util.Date;

public interface CustomerAnalyticsView {


    String getCustomerId();
    String getEmail();
    String getCity();
    Integer getTotalOrders();
    Double getMonetary();
    Double getAvgOrderValue();

    LocalDate getLastOrderDate();
    LocalDate getFirstOrderDate();

    Integer getRecency();
    Integer getCustomerTenureDays();

    Double getAvgSessionDuration();
    Double getAvgPagesViewed();
    Double getEngagementScore();

    Double getAvgRating();
    Double getAvgDeliveryDays();

    LocalDate getLastActivityDate();
    Boolean getIsReturningCustomer();
    Integer getRScore();
    Integer getFScore();
    Integer getMScore();
    String getMostUsedDevice();
    String getMostUsedPaymentMethod();
    String getTopProductCategory();
}
