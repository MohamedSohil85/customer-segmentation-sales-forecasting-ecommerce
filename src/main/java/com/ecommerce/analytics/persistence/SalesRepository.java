package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.Projection.PeakSalesView;
import com.ecommerce.analytics.Projection.SalesAnalyticsView;
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
            date AS salesDate,
            to_char( date,'Day') AS dayName,
            to_char(date,'Month') AS monthName,
            SUM(total_amount) AS totalSales,
            SUM(quantity) AS totalQuantity
        FROM ecommerce_dataset
        WHERE date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY totalSales DESC
        limit 5
        """, nativeQuery = true)
    List<PeakSalesView> findPeakSalesDays(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
