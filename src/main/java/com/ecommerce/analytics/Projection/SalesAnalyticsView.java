package com.ecommerce.analytics.Projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SalesAnalyticsView {

    Integer getYear();
    Integer getMonth();
    String getCategory();
    BigDecimal getTotalSales();
    BigDecimal getTotalQuantity();
}


