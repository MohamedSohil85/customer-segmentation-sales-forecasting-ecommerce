package com.ecommerce.analytics.Projection;

import java.time.LocalDate;

public interface PeakSalesView {

    LocalDate getSalesDate();
    String getDayName();
    String getMonthName();
    Double getTotalSales();
    Integer getTotalQuantity();

}
