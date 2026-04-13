package com.ecommerce.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerChurn implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Customer_ID;
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
