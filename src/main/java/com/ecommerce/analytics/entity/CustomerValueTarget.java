package com.ecommerce.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CustomerValueTarget implements Serializable {

    @Id
    private Long id;
    private Integer recency;
    private Integer frequency;//total orders
    private BigDecimal monetary;
    private BigDecimal avgOrderValue;
    private BigDecimal engagementScore;
    private BigDecimal avgRating;
    private Integer customerTenureDays;
    //Label
    private String customerValueLabel;

}
