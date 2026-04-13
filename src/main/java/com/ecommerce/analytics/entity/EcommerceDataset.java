package com.ecommerce.analytics.entity;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcommerceDataset implements Serializable {
    @Id
    @CsvBindByName(column = "Order_ID")
    private String Order_ID;
    @CsvBindByName(column = "Customer_ID")
    private String Customer_ID;
    @CsvBindByName(column = "Date")
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
    private String Unit_Price;
    @CsvBindByName(column = "Quantity")
    private String Quantity;
    @CsvBindByName(column = "Discount_Amount")
    private String Discount_Amount;
    @CsvBindByName(column = "Total_Amount")
    private String Total_Amount;
    @CsvBindByName(column = "Payment_Method")
    private String Payment_Method;
    @CsvBindByName(column = "Device_Type")
    private String Device_Type;
    @CsvBindByName(column = "Session_Duration_Minutes")
    private String Session_Duration_Minutes;
    @CsvBindByName(column = "Pages_Viewed")
    private String Pages_Viewed;
    @CsvBindByName(column = "Is_Returning_Customer")
    private String Is_Returning_Customer;
    @CsvBindByName(column = "Delivery_Time_Days")
    private String Delivery_Time_Days;
    @CsvBindByName(column = "Customer_Rating")
    private String Customer_Rating;
    @CsvBindByName(column = "Email")
    private String Email;

}
