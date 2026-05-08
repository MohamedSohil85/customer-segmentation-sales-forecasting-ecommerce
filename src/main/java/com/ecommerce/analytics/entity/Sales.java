package com.ecommerce.analytics.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
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
public class Sales implements Serializable {

    @Id
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    private String category;
    private int quantity;
    private double totalAmount;


}
