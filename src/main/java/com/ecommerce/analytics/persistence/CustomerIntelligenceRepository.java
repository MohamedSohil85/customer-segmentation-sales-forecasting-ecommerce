package com.ecommerce.analytics.persistence;


import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.entity.EcommerceDataset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerIntelligenceRepository extends JpaRepository<CustomerIntelligence, String> {


    @Query(value = """
    WITH bounds AS (
        SELECT MIN(o.date) AS startDate, MAX(o.date) AS endDate
        FROM ecommerce_dataset o
    )

    SELECT
        t.*,

        CASE
            WHEN t.recency <= 30 THEN 3
            WHEN t.recency <= 60 THEN 2
            ELSE 1
        END AS rScore,

        CASE
            WHEN t.totalOrders >= 5 THEN 3
            WHEN t.totalOrders >= 2 THEN 2
            ELSE 1
        END AS fScore,

        CASE
            WHEN t.monetary >= 500 THEN 3
            WHEN t.monetary >= 200 THEN 2
            ELSE 1
        END AS mScore

    FROM (
        SELECT
            COUNT(o.order_id) AS totalOrders,
            o.customer_id AS customerID,
            o.city AS city,
            o.email AS email,
            SUM(o.total_amount) AS monetary,
            AVG(o.total_amount) AS avgOrderValue,
            MAX(o.date) AS lastOrderDate,
            MIN(o.date) AS firstOrderDate,

            MAX(b.endDate) - MAX(o.date) AS recency,
            MAX(b.endDate) - MIN(o.date) AS customerTenureDays,

            AVG(o.session_duration_minutes) AS avgSessionDuration,
            AVG(o.pages_viewed) AS avgPagesViewed,

            AVG(
                (o.session_duration_minutes / 120.0) * 0.4 +
                (o.pages_viewed / 50.0) * 0.6
            ) * 100 AS engagementScore,

            AVG(o.customer_rating) AS avgRating,
            AVG(o.delivery_time_days) AS avgDeliveryDays,

            MAX(o.date) AS lastActivityDate,

            CASE
                WHEN COUNT(o.order_id) > 1 THEN TRUE
                ELSE FALSE
            END AS isReturningCustomer,
           MODE() WITHIN GROUP (ORDER BY payment_method) AS mostUsedPaymentMethod,
           MODE() WITHIN GROUP ( ORDER BY device_type) AS mostUsedDevice,
           MODE() WITHIN GROUP ( ORDER BY product_category) AS topProductCategory
        FROM ecommerce_dataset o
        CROSS JOIN bounds b
        GROUP BY o.customer_id, o.city, o.email
    ) t

    ORDER BY t.customerID
    """, nativeQuery = true)
    List<CustomerAnalyticsView> findAllCustomerIntelligence(Pageable pageable );
    @Modifying
    @Query("DELETE FROM CustomerIntelligence")
    void deleteAllCustomerIntelligence();
    @Query(value = "SELECT * FROM customer_intelligence c WHERE  c.customer_tenure_days < :days AND c.total_orders > :orders",nativeQuery = true)
    List<CustomerAnalyticsView> findFastGrowingCustomers(@Param("days") int days,@Param("orders") int orders);
    @Query(value = "SELECT * FROM customer_intelligence c  WHERE c.engagement_score > :engagement AND c.monetary > :monetary AND c.last_order_date >= :inactivity ",nativeQuery = true)
    List<CustomerAnalyticsView> findBestCustomers(@Param("engagement") double engagement,@Param("monetary") BigDecimal monetary,@Param("lastOrderDate") LocalDate lastOrderDate);
    @Query(value = "select * from customer_intelligence c where c.last_order_date > :lastOrderDate and c.engagement_score < :score and c.recency > :recency",nativeQuery = true)
    List<CustomerAnalyticsView> findAtRiskCustomers(@Param("lastOrderDate") LocalDate date,@Param("score") int score,@Param("recency") int recency);
     }
