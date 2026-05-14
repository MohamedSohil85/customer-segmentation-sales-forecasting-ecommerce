package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.Projection.CustomerSegmentAnalytics;
import com.ecommerce.analytics.entity.CustomerSegmentation;
import com.ecommerce.analytics.service.CustomerSegmentationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;



@Repository
public interface CustomerSegmentationRepository extends JpaRepository<CustomerSegmentation, Long> {

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
            SUM(o.total_amount) AS monetary,
            AVG(o.total_amount) AS avgOrderValue,
            MAX(b.endDate) - MAX(o.date) AS recency,
            MAX(b.endDate) - MIN(o.date) AS customerTenureDays,

            AVG(o.session_duration_minutes) AS avgSessionDuration,
            AVG(o.pages_viewed) AS avgPagesViewed,

            AVG(
                (o.session_duration_minutes / 120.0) * 0.4 +
                (o.pages_viewed / 50.0) * 0.6
            ) * 100 AS engagementScore,

            AVG(o.customer_rating) AS avgRating,
            MAX(o.date) AS lastActivityDate,

            CASE
                WHEN COUNT(o.order_id) > 1 THEN TRUE
                ELSE FALSE
            END AS isReturningCustomer

        FROM ecommerce_dataset o
        CROSS JOIN bounds b
        GROUP BY o.customer_id
    ) t

    ORDER BY t.customerID
    """, nativeQuery = true)
    List<CustomerSegmentAnalytics>findAllCustomerSegmentations(Pageable pageable);
    Page<CustomerSegmentation>findBySegmentNameIgnoreCase(Pageable pageable, String segmentName);
    List<CustomerSegmentation>findTopByOrderBySegmentScoreDesc(Pageable pageable);
    List<CustomerSegmentation>findCustomerSegmentationByChurnRiskLevel(String level);
    List<CustomerSegmentation>findCustomerSegmentationBySegmentScoreGreaterThan(Integer score);
    List<CustomerSegmentation>findCustomerSegmentationByCustomerValueLabel(String level);
    @Query(value = "DELETE FROM customer_segmentation",nativeQuery = true)
    @Modifying
    void deleteCustomerSegmentation();
    List<CustomerSegmentation>findCustomerSegmentationByEngagementLevel(Pageable pageable, String engagementLevel);
}
