package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.PeakSalesView;
import com.ecommerce.analytics.Projection.SalesAnalyticsView;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.entity.Sales;
import com.ecommerce.analytics.persistence.SalesRepository;
import com.ecommerce.analytics.persistence.eCommerceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalesService {

    private final SalesRepository salesRepository;


    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;

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

}
