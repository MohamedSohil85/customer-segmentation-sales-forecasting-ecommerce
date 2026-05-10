package com.ecommerce.analytics.Projection;

import java.math.BigDecimal;

public interface SeasonalSalesAnalyticsView {

    String getSeason();
    Integer getYear();
    Integer getMonth();
    Long getTotalOrders();
    BigDecimal getTotalSales();
    BigDecimal getAverageOrderValue();
    BigDecimal getMinOrderValue();
    BigDecimal getMaxOrderValue();
}
