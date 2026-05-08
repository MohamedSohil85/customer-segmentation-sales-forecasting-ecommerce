package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.entity.CustomerChurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerChurnRepository extends JpaRepository<CustomerChurn, Integer> {
}
