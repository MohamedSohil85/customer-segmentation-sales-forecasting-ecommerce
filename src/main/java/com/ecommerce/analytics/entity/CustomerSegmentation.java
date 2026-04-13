package com.ecommerce.analytics.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSegmentation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Customer_ID;
    private String segmentName;
    private Integer segmentScore;
    private String churnRiskLevel;
    private String engagementLevel;
    private String customerValueLabel;
    private LocalDateTime segmentationDate;
}
