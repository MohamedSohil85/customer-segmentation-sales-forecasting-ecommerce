package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.*;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.persistence.SalesRepository;
import com.ecommerce.analytics.persistence.eCommerceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SalesService {

    private final SalesRepository salesRepository;
    private final eCommerceRepository eCommerceRepository;

    public SalesService(SalesRepository salesRepository, com.ecommerce.analytics.persistence.eCommerceRepository eCommerceRepository) {
        this.salesRepository = salesRepository;

        this.eCommerceRepository = eCommerceRepository;
    }

    public List<SalesAnalyticsView> getAllSales(String category , LocalDate from, LocalDate to){
        return salesRepository.findSalesBy(category, from, to);
    }
   public List<SalesAnalyticsView>findTopCategories( LocalDate from, LocalDate to){
        return salesRepository.findTopCategorySalesByDate(from, to);
   }
   public List<SalesAnalyticsView>findCategorySalesTrend( LocalDate from, LocalDate to){
        return salesRepository.findCategorySalesTrend(from,to);
   }
   public List<PeakSalesView>findPeakSalesDays(LocalDate from, LocalDate to){
        return salesRepository.findPeakSalesDays(from,to);
   }
  public List<SeasonalSalesAnalyticsView>findSeasonalSalesDays(LocalDate from, LocalDate to){
        return salesRepository.findSeasonalSalesAnalytics(from,to);
  }

   public List<YearMonth>findPeakSalesMonths(LocalDate from, LocalDate to){
       Map<YearMonth, BigDecimal> monthly = getMonthlySales(from,to);
       return monthly.entrySet()
               .stream()
               .sorted((a, b)-> b.getValue().compareTo(a.getValue()))
               .map(Map.Entry::getKey)
               .collect(Collectors.toList());
   }

    public Map<YearMonth, BigDecimal> getMonthlySales( LocalDate from, LocalDate to) {
       List<EcommerceDataset>list= salesRepository.findEcommerceDataset(from,to);
        //
        return list.stream()
                .collect(Collectors.groupingBy(
                        ecommerceDataset -> YearMonth.from(ecommerceDataset.getDate()),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                EcommerceDataset::getTotal_Amount,
                                BigDecimal::add
                        )
                ));
    }


    //find the best Month , worst day  in the year
    public Map<String, EcommerceDataset> findBestAndWorstDay(
            LocalDate startDate,
            LocalDate endDate){

        List<EcommerceDataset>list=salesRepository.findEcommerceDataset(startDate,endDate);
        EcommerceDataset bestMonth= list.stream().max(Comparator.comparing(EcommerceDataset::getTotal_Amount)).orElse(null);
        EcommerceDataset worstMonth=list.stream().min(Comparator.comparing(EcommerceDataset::getTotal_Amount)).orElse(null);
        Map<String, EcommerceDataset> result=new HashMap<>();
        result.put("Best sales Day of the year",bestMonth);
        result.put("Lowest sales Day of the year",worstMonth);
        return result;



    }
    public Map<String, SalesDashboardView>getYearlySalesOverview(LocalDate startDate, LocalDate endDate){
     List<EcommerceDataset>list=salesRepository.findEcommerceDataset(startDate,endDate);

        BigDecimal totalRevenue = list.stream()
                .map(EcommerceDataset::getTotal_Amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long totalOrders=list.stream().map(EcommerceDataset::getOrder_id).count();

        Long totalQuantity= list.stream().mapToLong(EcommerceDataset::getQuantity).sum();

        String topCategory=list.stream()
                .collect(Collectors.groupingBy(EcommerceDataset::getProduct_Category,Collectors.reducing(BigDecimal.ZERO,EcommerceDataset::getTotal_Amount,BigDecimal::add)))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Month bestMonth = list.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                EcommerceDataset::getTotal_Amount,
                                BigDecimal::add
                        )
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        Month worstMonth=list
                .stream()
                .collect(Collectors.groupingBy(ecommerceDataset -> ecommerceDataset.getDate().getMonth(),Collectors.reducing(BigDecimal.ZERO,EcommerceDataset::getTotal_Amount,BigDecimal::add)))
                .entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        List<EcommerceDataset> customers =
                salesRepository.findEcommerceDataset(startDate, endDate);

        long totalCustomers = customers.stream()
                .map(EcommerceDataset::getCustomer_ID)
                .distinct()
                .count();
        long returningCustomers = customers.stream()
                .filter(customer ->
                        "Yes".equalsIgnoreCase(
                                customer.getIs_Returning_Customer()
                        )
                )
                .map(EcommerceDataset::getCustomer_ID)
                .distinct()
                .count();

        double returningCustomerRate=(returningCustomers * 100.0) / totalCustomers;


        SalesDashboardView salesDashboardView=new SalesDashboardView();
        salesDashboardView.setTopCategory(topCategory);
        salesDashboardView.setBestSellerMonth(bestMonth);
        salesDashboardView.setWorstSellerMonth(worstMonth);
        salesDashboardView.setTotalRevenue(totalRevenue);
        salesDashboardView.setTotalOrders(totalOrders);
        salesDashboardView.setReturningCustomerRate(returningCustomerRate);
        salesDashboardView.setTotalQuantitySold(totalQuantity);

        Map<String,SalesDashboardView>result=new HashMap<>();
        result.put("Sales from :"+startDate+" ,to :"+endDate,salesDashboardView);
        return result;





    }
}
