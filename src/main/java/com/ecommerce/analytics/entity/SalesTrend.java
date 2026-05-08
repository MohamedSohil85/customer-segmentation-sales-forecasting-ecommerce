package com.ecommerce.analytics.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SalesTrend implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private Long id;
    @CsvBindByName(column = "Date")
    @CsvDate("yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    @CsvBindByName(column = "Total_Amount")
    private double salesAmount;
}
