package com.ecommerce.analytics.service;

import com.ecommerce.analytics.Projection.CustomerAnalyticsView;
import com.ecommerce.analytics.entity.CustomerIntelligence;
import com.ecommerce.analytics.entity.EcommerceDataset;
import com.ecommerce.analytics.persistence.CustomerIntelligenceRepository;
import com.ecommerce.analytics.persistence.eCommerceRepository;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class EcommerceService {

    private final eCommerceRepository eCommerceRepository;
    private final CustomerIntelligenceRepository ciRepository;

    public EcommerceService(com.ecommerce.analytics.persistence.eCommerceRepository eCommerceRepository, CustomerIntelligenceRepository customerIntelligenceRepository) {
        this.eCommerceRepository = eCommerceRepository;
        this.ciRepository = customerIntelligenceRepository;
    }
    @Transactional
    public List<EcommerceDataset>readCSVFile() throws FileNotFoundException {
        List<EcommerceDataset> ecommerceDatasets= new CsvToBeanBuilder<EcommerceDataset>(
                new FileReader(toPath().toFile()))
                .withType(EcommerceDataset.class)
                .build()
                .parse();
       eCommerceRepository.saveAll(ecommerceDatasets);
       ciRepository.deleteAllCustomerIntelligence();
       refreshCustomerIntelligence();
       return ecommerceDatasets;
    }


    public  EcommerceDataset saveNewData(EcommerceDataset ecommerceDataset) {

        CustomerIntelligence customerIntelligence = new CustomerIntelligence();
        customerIntelligence.setCustomerId(ecommerceDataset.getCustomer_ID());

        eCommerceRepository.save(ecommerceDataset);
        ciRepository.deleteAllCustomerIntelligence();
        refreshCustomerIntelligence();
        return ecommerceDataset;

    }
    public void refreshCustomerIntelligence() {
        List<CustomerAnalyticsView> analytics =
                ciRepository.findAllCustomerIntelligence(Pageable.unpaged());

        List<CustomerIntelligence> entities = analytics.stream()
                .map(this::mapToEntity)
                .toList();

        ciRepository.saveAll(entities);
    }
    private CustomerIntelligence mapToEntity(CustomerAnalyticsView view) {
        CustomerIntelligence ci = new CustomerIntelligence();

        ci.setCustomerId(view.getCustomerId());
        ci.setTotalOrders(view.getTotalOrders());
        ci.setMonetary(BigDecimal.valueOf(view.getMonetary()));
        ci.setAvgOrderValue(BigDecimal.valueOf(view.getAvgOrderValue()));

        ci.setRecency(view.getRecency());
        ci.setCustomerTenureDays(view.getCustomerTenureDays());

        ci.setRScore(view.getRScore());
        ci.setFScore(view.getFScore());
        ci.setMScore(view.getMScore());

        ci.setAvgSessionDuration(BigDecimal.valueOf(view.getAvgSessionDuration()));
        ci.setAvgPagesViewed(BigDecimal.valueOf(view.getAvgPagesViewed()));
        ci.setEngagementScore(view.getEngagementScore());

        ci.setAvgRating(BigDecimal.valueOf(view.getAvgRating()));
        ci.setAvgDeliveryDays(BigDecimal.valueOf(view.getAvgDeliveryDays()));

        ci.setFirstOrderDate(view.getFirstOrderDate());
        ci.setLastOrderDate(view.getLastOrderDate());
        ci.setLastActivityDate(view.getLastActivityDate());
        ci.setIsReturningCustomer(view.getIsReturningCustomer());
        ci.setMostUsedDevice(view.getMostUsedDevice());
        ci.setMostUsedPaymentMethod(view.getMostUsedPaymentMethod());
        ci.setTopProductCategory(view.getTopProductCategory());

        return ci;
    }


    private static Path toPath() {
        try {
            URL fileUrl = EcommerceService.class.getClassLoader().getResource("ecommerce_customers_2025_2026.csv");
            assert fileUrl != null;
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
