package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.entity.CustomerIntelligence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerIntelligenceRepository extends JpaRepository<CustomerIntelligence, String> {

    Page<CustomerIntelligence> getAllCustomers(Pageable pageable);
    Page<CustomerIntelligence> findByIsReturningCustomer(Boolean isReturningCustomer, Pageable pageable);
    Page<CustomerIntelligence> findByRecencyLessThan(Integer days, Pageable pageable); //Active customers
    Page<CustomerIntelligence> findByIsReturningCustomerTrue(Pageable pageable);
    List<CustomerIntelligence> findByIsReturningCustomerTrue();
    List<CustomerIntelligence> findByIsReturningCustomerFalse();
    Page<CustomerIntelligence> findByMonetaryGreaterThan(BigDecimal monetary, Pageable pageable);
    @Query("""
    SELECT c FROM CustomerIntelligence c
    WHERE c.monetary > :amount
    AND c.engagementScore > :engagement
""")
    Page<CustomerIntelligence> findHighValueCustomers(
            @Param("amount") Double amount,
            @Param("engagement") Double engagement,
            Pageable pageable
    );
}
