package com.ecommerce.analytics.ml;


import com.opencsv.bean.CsvBindByName;
import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerRecord{

        @CsvBindByName(column = "Age")
        private int age;
        @CsvBindByName(column = "Gender")
        private String gender;
        @CsvBindByName(column = "City")
        private String city;
        @CsvBindByName(column = "Product_Category")
       private String category;
        @CsvBindByName(column = "Payment_Method")
        private String paymentMethod;
        @CsvBindByName(column = "Device_Type")
        private String deviceType;
        @CsvBindByName(column = "Session_Duration_Minutes")
        private double sessionDuration;
        @CsvBindByName(column = "Pages_Viewed")
        private int pagesViewed;
        @CsvBindByName(column = "Delivery_Time_Days")
        private int deliveryTime;
        @CsvBindByName(column = "Customer_Rating")
        private double rating;
        @CsvBindByName(column = "Is_Returning_Customer")
        private String isReturningCustomer;


 }
