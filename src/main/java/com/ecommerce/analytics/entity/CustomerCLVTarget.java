package com.ecommerce.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCLVTarget implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer recency;
    private Integer frequency;
    private BigDecimal engagementScore;
    private BigDecimal avgRating;
    private Integer customerTenureDays;
    private BigDecimal avgSessionDuration;
    private Integer pagesViewed;

    // Target
    private Double clv;
}
