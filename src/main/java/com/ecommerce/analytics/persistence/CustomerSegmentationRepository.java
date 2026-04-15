package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.entity.CustomerSegmentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;



@Repository
public interface CustomerSegmentationRepository extends JpaRepository<CustomerSegmentation, Long> {

    // =========================================
    // 🔹 BASIC
    // =========================================

    Page<CustomerSegmentation> findTopByCustomerID(Pageable pageable);
    List<CustomerSegmentation> findByCustomerID(String customer_id);

    // =========================================
    // 🔹 SEGMENT NAME
    // =========================================

    List<CustomerSegmentation> findBySegmentName(String segmentName);

    Page<CustomerSegmentation> findBySegmentName(String segmentName, Pageable pageable);

    List<CustomerSegmentation> findBySegmentNameIn(List<String> segments);

    // =========================================
    // 🔹 SEGMENT SCORE
    // =========================================

    List<CustomerSegmentation> findBySegmentScoreGreaterThan(Integer score);

    List<CustomerSegmentation> findBySegmentScoreBetween(Integer min, Integer max);

    List<CustomerSegmentation> findTop10ByOrderBySegmentScoreDesc();

    // =========================================
    // 🔹 CHURN RISK
    // =========================================

    List<CustomerSegmentation> findByChurnRiskLevel(String level);
    List<CustomerSegmentation> findByChurnRiskLevelIn(List<String> levels);

    // High risk customers
    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.churnRiskLevel = 'HIGH'
    """)
    List<CustomerSegmentation> findHighRiskCustomers();
    // =========================================
    // 🔹 ENGAGEMENT
    // =========================================

    List<CustomerSegmentation> findByEngagementLevel(String level);

    List<CustomerSegmentation> findByEngagementLevelIn(List<String> levels);

    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.engagementLevel = 'LOW'
    """)
    List<CustomerSegmentation> findLowEngagementCustomers();

    // =========================================
    // 🔹 CUSTOMER VALUE
    // =========================================

    List<CustomerSegmentation> findByCustomerValueLabel(String label);
    List<CustomerSegmentation> findByCustomerValueLabelIn(List<String> labels);

    // =========================================
    // 🔹 COMBINED SEGMENTATION (IMPORTANT)
    // =========================================

    // High value + low engagement (Upsell opportunity)
    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.customerValueLabel = 'High'
        AND c.engagementLevel = 'LOW'
    """)
    List<CustomerSegmentation> findHighValueLowEngagement();

    // High churn risk + high value (Critical segment)
    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.customerValueLabel = 'High'
        AND c.churnRiskLevel = 'HIGH'
    """)
    List<CustomerSegmentation> findHighValueHighRisk();

    // Loyal customers
    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.engagementLevel = 'HIGH'
        AND c.churnRiskLevel = 'LOW'
    """)
    List<CustomerSegmentation> findLoyalCustomers();

    // =========================================
    // 🔹 DATE FILTERING
    // =========================================

    List<CustomerSegmentation> findBySegmentationDate(LocalDate segmentationDate);

    List<CustomerSegmentation> findBySegmentationDateBetween(
            LocalDate start,
            LocalDate end
    );

    List<CustomerSegmentation> findTop10ByOrderBySegmentationDateDesc();

    // Latest segmentation per customer
    @Query("""
        SELECT c FROM CustomerSegmentation c
        WHERE c.segmentationDate = (
            SELECT MAX(c2.segmentationDate)
            FROM CustomerSegmentation c2
            WHERE c2.CustomerID = c.CustomerID
        )
    """)
    List<CustomerSegmentation> findLatestSegmentationPerCustomer();

    // =========================================
    // 🔹 DASHBOARD / ANALYTICS
    // =========================================

    // Count by segment
    @Query("""
        SELECT c.segmentName, COUNT(c)
        FROM CustomerSegmentation c
        GROUP BY c.segmentName
    """)
    List<Object[]> countBySegment();

    // Count by churn risk
    @Query("""
        SELECT c.churnRiskLevel, COUNT(c)
        FROM CustomerSegmentation c
        GROUP BY c.churnRiskLevel
    """)
    List<Object[]> countByChurnRisk();

    // Count by engagement
    @Query("""
        SELECT c.engagementLevel, COUNT(c)
        FROM CustomerSegmentation c
        GROUP BY c.engagementLevel
    """)
    List<Object[]> countByEngagement();

    // Count by value
    @Query("""
        SELECT c.customerValueLabel, COUNT(c)
        FROM CustomerSegmentation c
        GROUP BY c.customerValueLabel
    """)
    List<Object[]> countByCustomerValue();

    // =========================================
    // 🔹 ADVANCED ANALYTICS
    // =========================================

    // Average segment score
    @Query("SELECT AVG(c.segmentScore) FROM CustomerSegmentation c")
    Double getAverageSegmentScore();

    // Max segment score
    @Query("SELECT MAX(c.segmentScore) FROM CustomerSegmentation c")
    Integer getMaxSegmentScore();

    // Min segment score
    @Query("SELECT MIN(c.segmentScore) FROM CustomerSegmentation c")
    Integer getMinSegmentScore();

    // =========================================
    // 🔹 PAGINATION FILTERS
    // =========================================

    Page<CustomerSegmentation> findByChurnRiskLevel(
            String level, Pageable pageable);

    Page<CustomerSegmentation> findByEngagementLevel(
            String level, Pageable pageable);

    Page<CustomerSegmentation> findByCustomerValueLabel(
            String label, Pageable pageable);

}
