package com.ecommerce.analytics.Projection;

import com.ecommerce.analytics.entity.EcommerceDataset;
import lombok.*;

import java.math.BigDecimal;
import java.time.Month;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SalesDashboardView {

     BigDecimal totalRevenue;
     Long totalOrders;
     Long totalQuantitySold;
     Double returningCustomerRate;
     Month bestSellerMonth;
     Month worstSellerMonth;
     String topCategory;
}

