package com.progweb.siri_cascudo_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.progweb.siri_cascudo_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
