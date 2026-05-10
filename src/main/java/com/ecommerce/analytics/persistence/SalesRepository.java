package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.Projection.PeakSalesView;
import com.ecommerce.analytics.Projection.SalesAnalyticsView;
import com.ecommerce.analytics.Projection.SeasonalSalesAnalyticsView;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.entity.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sales,Long> {

    @Query(value = "SELECT product_category as Category ,extract(year from date) as year,extract(month from date) as month ,SUM(total_amount) AS total_sales ,sum(quantity) as total_quantity  FROM ecommerce_dataset WHERE " +
            "    product_category = :category "+
            "    AND date BETWEEN :startDate AND :endDate group by product_category,EXTRACT(YEAR FROM date),\n" +
            "    EXTRACT(MONTH FROM date)  order by year , month",nativeQuery = true)
    List<SalesAnalyticsView> findSalesBy(String category, LocalDate startDate, LocalDate endDate);

    @Query(value = "SELECT product_category as Category ,extract(year from date) as year,extract(month from date) as month ,SUM(total_amount) AS total_sales ,sum(quantity) as total_quantity FROM ecommerce_dataset " +
            "    where date BETWEEN :startDate AND :endDate group by product_category, EXTRACT(YEAR FROM date),  EXTRACT(MONTH FROM date) order by month,year,sum(total_amount) DESC",nativeQuery = true)
    List<SalesAnalyticsView> findTopCategorySalesByDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
    @Query(value = """
        SELECT
            product_category AS category,
            EXTRACT(YEAR FROM date) AS year,
            EXTRACT(MONTH FROM date) AS month,
            SUM(total_amount) AS totalSales,
            SUM(quantity) AS totalQuantity
        FROM ecommerce_dataset
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY
            product_category,
            EXTRACT(YEAR FROM date),
            EXTRACT(MONTH FROM date)
        ORDER BY
            category,
            year,
            month
        """, nativeQuery = true)
    List<SalesAnalyticsView> findTopCategoryBySalesByDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
    SELECT
        DATE_TRUNC('month', date)::date AS salesDate,
        to_char(date ,'FMMonth') AS monthName,
        SUM(total_amount) AS totalSales,
        SUM(quantity) AS totalQuantity
    FROM ecommerce_dataset
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY DATE_TRUNC('month', date),monthName
    ORDER BY totalSales DESC
    """, nativeQuery = true)
    List<PeakSalesView> findPeakSalesDays(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = """
    SELECT
        CASE
            /* =========================
               NEW YEAR
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 1
                AND EXTRACT(DAY FROM date) BETWEEN 1 AND 7
            )
            THEN 'New Year Season'
            /* =========================
               WINTER SALE
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 1
                AND EXTRACT(DAY FROM date) > 7
            )
            THEN 'Winter Sale'

            /* =========================
               VALENTINE
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 2
                AND EXTRACT(DAY FROM date) BETWEEN 1 AND 14
            )
            THEN 'Valentine Season'

            /* =========================
               EASTER (floating holiday window)
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 4
                AND EXTRACT(DAY FROM date) BETWEEN 1 AND 20
            )
            THEN 'Easter Season'
            /* =========================
               SPRING
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 4
                OR EXTRACT(MONTH FROM date) = 5
            )
            THEN 'Spring Collection'

            /* =========================
               MOTHER DAY
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 5
            )
            THEN 'Mother Day Season'
            /* =========================
               SUMMER SALE
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) BETWEEN 6 AND 8
            )
            THEN 'Summer Sale'
            /* =========================
               BACK TO SCHOOL
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) BETWEEN 8 AND 9
            )
            THEN 'Back To School'

            /* =========================
               AUTUMN
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 9
            )
            THEN 'Autumn Collection'

            /* =========================
               HALLOWEEN
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 10
            )
            THEN 'Halloween Season'
            /* =========================
               SINGLES DAY
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 11
                AND EXTRACT(DAY FROM date) = 11
            )
            THEN 'Singles Day'
            /* =========================
               BLACK FRIDAY WINDOW
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 11
                AND EXTRACT(DAY FROM date) BETWEEN 20 AND 30
            )
            THEN 'Black Friday Season'

            /* =========================
               CHRISTMAS
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 12
                AND EXTRACT(DAY FROM date) BETWEEN 1 AND 25
            )
            THEN 'Christmas Season'
            /* =========================
               YEAR END
               ========================= */
            WHEN (
                EXTRACT(MONTH FROM date) = 12
            )
            THEN 'Year End Sale'
            /* =========================
               DEFAULT
               ========================= */
            ELSE 'Regular Season'

        END AS season,

        EXTRACT(YEAR FROM date) AS year,
        EXTRACT(MONTH FROM date) AS month,
        COUNT(*) AS totalOrders,
        SUM(total_amount) AS totalSales,
        AVG(total_amount) AS averageOrderValue,
        MIN(total_amount) AS minOrderValue,
        MAX(total_amount) AS maxOrderValue

    FROM ecommerce_dataset
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY
        season,
        EXTRACT(YEAR FROM date),
        EXTRACT(MONTH FROM date)
    ORDER BY
        totalSales DESC,
        year,
        month

""", nativeQuery = true)
    List<SeasonalSalesAnalyticsView> findSeasonalSalesAnalytics(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
   @Query(value = "SELECT * from ecommerce_dataset where date between :startDate and :endDate",nativeQuery = true)
    List<EcommerceDataset> findEcommerceDataset(@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate);

    @Query(value = """
    SELECT
        to_char(date,'Month') AS monthName,
        to_char(date,'Day') AS dayName,
        SUM(total_amount) AS totalSales
    FROM ecommerce_dataset
    WHERE date BETWEEN :startDate AND :endDate
    GROUP BY monthName, date
    ORDER BY totalSales DESC
    """, nativeQuery = true)
    List<PeakSalesView>findPeakSalesViewsMonthly(@Param("startDate")LocalDate startDate,@Param("endDate")LocalDate endDate);
}
