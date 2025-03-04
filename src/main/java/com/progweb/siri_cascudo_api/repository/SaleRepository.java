package com.progweb.siri_cascudo_api.repository;
import com.progweb.siri_cascudo_api.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Override
    Optional<Sale> findById(Long idSale);
    
    List<Sale> findByDateBetween(Date start, Date end);
}
