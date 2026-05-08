package com.ecommerce.analytics.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcommerceDataset implements Serializable {
    @Id
    @CsvBindByName(column = "Order_ID")
    private String order_id;
    @CsvBindByName(column = "Customer_ID")
    private String Customer_ID;
    @CsvBindByName(column = "Date")
    @CsvDate("yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate Date;
    @CsvBindByName(column = "Age")
    private int Age;
    @CsvBindByName(column = "Gender")
    private String Gender;
    @CsvBindByName(column = "City")
    private String City;
    @CsvBindByName(column = "Product_Category")
    private String Product_Category;
    @CsvBindByName(column = "Unit_Price")
    private BigDecimal Unit_Price;
    @CsvBindByName(column = "Quantity")
    private int Quantity;
    @CsvBindByName(column = "Discount_Amount")
    private BigDecimal Discount_Amount;
    @CsvBindByName(column = "Total_Amount")
    private BigDecimal Total_Amount;
    @CsvBindByName(column = "Payment_Method")
    private String Payment_Method;
    @CsvBindByName(column = "Device_Type")
    private String Device_Type;
    @CsvBindByName(column = "Session_Duration_Minutes")
    private int Session_Duration_Minutes;
    @CsvBindByName(column = "Pages_Viewed")
    private int Pages_Viewed;
    @CsvBindByName(column = "Is_Returning_Customer")
    private String Is_Returning_Customer;
    @CsvBindByName(column = "Delivery_Time_Days")
    private int Delivery_Time_Days;
    @CsvBindByName(column = "Customer_Rating")
    private BigDecimal Customer_Rating;
    @CsvBindByName(column = "Email")
    private String Email;

}
