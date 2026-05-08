package com.ecommerce.analytics.ml;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerChurnPrediction {


    private Integer recency;
    private Integer frequency;
    private BigDecimal engagementScore;
    private BigDecimal avgRating;
    private Integer customerTenureDays;
    private Integer inactivityDays;
    private BigDecimal avgSessionDuration;
    private Integer pagesViewed;
    private String churnLabel;
}
