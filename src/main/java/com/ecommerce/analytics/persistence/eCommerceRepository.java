package com.ecommerce.analytics.persistence;

import com.ecommerce.analytics.entity.EcommerceDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface eCommerceRepository extends JpaRepository<EcommerceDataset,String> {
}
