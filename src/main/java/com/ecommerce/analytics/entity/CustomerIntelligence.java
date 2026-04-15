package com.ecommerce.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class CustomerIntelligence implements Serializable {

    // Primary Key
    @Id
    private String customerId;

    // RFM Metrics
    private Integer recency;
    private Integer totalOrders;
    private BigDecimal monetary; //total amount
    private BigDecimal avgOrderValue;
    private Integer rScore;
    private Integer fScore;
    private Integer mScore;

    // Engagement
    private BigDecimal avgSessionDuration;
    private BigDecimal avgPagesViewed;
    private BigDecimal engagementScore;
    private LocalDate lastActivityDate;
    private Integer inactivityDays;

    // Experience
    private BigDecimal avgRating;
    private BigDecimal avgDeliveryDays;

    // Customer Lifecycle
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
    private Integer customerTenureDays;
    private Boolean isReturningCustomer;

    // Preferences
    private String mostUsedPaymentMethod;
    private String mostUsedDevice;
    private String topProductCategory;
}
