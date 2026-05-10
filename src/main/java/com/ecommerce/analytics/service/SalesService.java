package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.PeakSalesView;
import com.ecommerce.analytics.Projection.SalesAnalyticsView;
import com.ecommerce.analytics.Projection.SeasonalSalesAnalyticsView;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.entity.Sales;
import com.ecommerce.analytics.persistence.SalesRepository;
import com.ecommerce.analytics.persistence.eCommerceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
   public List<SalesAnalyticsView>findTopSalesByDate( LocalDate from, LocalDate to){
        return salesRepository.findTopCategoryBySalesByDate(from,to);
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

   public List<PeakSalesView>findPeakSalesMonthly(LocalDate from,LocalDate to){
        return salesRepository.findPeakSalesViewsMonthly(from,to);
    }
    //find the best Month , worst Month , peak sales , and best days in the year

}
