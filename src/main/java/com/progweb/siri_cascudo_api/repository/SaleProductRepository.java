package com.progweb.siri_cascudo_api.repository;

import com.progweb.siri_cascudo_api.model.SaleProduct;
import com.progweb.siri_cascudo_api.model.SaleProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleProductRepository extends JpaRepository<SaleProduct, SaleProductId> {
    List<SaleProduct> findById_IdSale(Long idProduct);

    @Override
    <S extends SaleProduct> List<S> saveAll(Iterable<S> entities);

    @Override
    void deleteAll(Iterable<? extends SaleProduct> entities);

    @Override
    void delete(SaleProduct entity);

    @Override
    void deleteAllById(Iterable<? extends SaleProductId> saleProductIds);
}