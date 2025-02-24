package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Sale.CreateSaleDTO;
import com.progweb.siri_cascudo_api.dto.Sale.SaleDTO;
import com.progweb.siri_cascudo_api.dto.Sale.SaleProductDTO;
import com.progweb.siri_cascudo_api.dto.Sale.UpdateSaleDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.exception.CustomException;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.*;
import com.progweb.siri_cascudo_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleProductRepository saleProductRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(this::mapToSaleDTO)
                .collect(Collectors.toList());
    }

    public UpdateSaleDTO getSaleById(Long saleId) {

        Sale saleById = saleRepository.findById(saleId).orElseThrow(
                () -> new ResourceNotFoundException("Venda não encontrada", "id", saleId));

        UpdateSaleDTO response = new UpdateSaleDTO();
        List<SaleProduct> products = saleProductRepository.findById_IdSale(saleId);

        response.setIdUser(saleById.getIdUser());
        response.setDate(saleById.getDate());
        response.setPaymentMethod(saleById.getPaymentMethod());
        response.setProducts(products.stream().map(this::mapToSaleProductDTO).collect(Collectors.toList()));

        return response;
    }

    public CreateResponseDTO createSale(CreateSaleDTO dto) {
        userRepository.findById(dto.getIdUser())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Venda não encontrada", "id", dto.getIdUser()));

        Sale newSale = new Sale();
        newSale.setDate(new Date());
        newSale.setPaymentMethod(dto.getPaymentMethod());
        newSale.setIdUser(dto.getIdUser());

        Sale createdSale = saleRepository.save(newSale);

        List<SaleProduct> productsSold = getSaleProductsByItems(dto.getProducts(), createdSale.getId());

        saleProductRepository.saveAll(productsSold);

        return new CreateResponseDTO(createdSale.getId().toString(), "Venda criada com sucesso.");
    }

    public List<SaleProduct> getSaleProductsByItems(List<SaleProductDTO> products, Long idSale) {
        return products.stream()
                .map(SaleProductDTO -> mapToSaleProduct(SaleProductDTO, idSale))
                .collect(Collectors.toList());
    }

    public List<SaleProductDTO> getSaleProductsDTOs(List<SaleProduct> products) {
        return products.stream()
                .map(this::mapToSaleProductDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UpdateResponseDTO<UpdateSaleDTO> updateSale(UpdateSaleDTO dto, Long saleId) {

        Sale saleById = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada", "id", saleId));

        if (!Objects.equals(dto.getIdUser(), saleById.getIdUser()) && dto.getIdUser() != null) {
            saleById.setIdUser(dto.getIdUser());
        }
        if (dto.getDate() != null && !dto.getDate().equals(saleById.getDate())) {
            saleById.setDate(dto.getDate());
        }
        if (!Objects.equals(dto.getPaymentMethod(), saleById.getPaymentMethod()) && dto.getPaymentMethod() != null) {
            saleById.setPaymentMethod(dto.getPaymentMethod());
        }

        List<SaleProduct> existingProducts = saleProductRepository.findById_IdSale(saleById.getId());

        if (dto.getProducts() != null) {
            List<SaleProduct> newProducts = getSaleProductsByItems(dto.getProducts(), saleById.getId());

            // Identificar os produtos removidos
            List<SaleProduct> removedProducts = existingProducts.stream()
                    .filter(product -> !newProducts.contains(product))
                    .toList();

            // Restaurar o estoque dos produtos removidos
            restoreStockAfterSaleDeletion(removedProducts);

            // Excluir os produtos removidos do banco de dados
            saleProductRepository.deleteAll(removedProducts);

            // Persistir os novos produtos e atualizar o estoque
            updateStockAfterSale(newProducts);
            saleProductRepository.saveAll(newProducts);
        }

        Sale result = saleRepository.save(saleById);

        UpdateSaleDTO response = new UpdateSaleDTO();
        response.setIdUser(result.getIdUser());
        response.setDate(result.getDate());
        response.setPaymentMethod(result.getPaymentMethod());
        response.setProducts(
                saleProductRepository.findById_IdSale(saleById.getId())
                        .stream()
                        .map(this::mapToSaleProductDTO)
                        .collect(Collectors.toList())
        );

        return new UpdateResponseDTO<>(saleId.toString(), "Venda atualizada com sucesso.", response);
    }

    @Transactional
    public void restoreStockAfterSaleDeletion(List<SaleProduct> productsSold) {
        for (SaleProduct saleProduct : productsSold) {
            Product product = productRepository.findById(saleProduct.getId().getIdProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado", "id", saleProduct.getId().getIdProduct()));

            product.setQuantity(product.getQuantity() + saleProduct.getQuantity());
            productRepository.save(product);
        }
    }

    @Transactional
    public void updateStockAfterSale(List<SaleProduct> productsSold) {
        for (SaleProduct saleProduct : productsSold) {
            Product product = productRepository.findById(saleProduct.getId().getIdProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado", "id", saleProduct.getId().getIdProduct()));

            if (product.getQuantity() < saleProduct.getQuantity()) {
                throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Estoque insuficiente", "A quantidade solicitada excede o estoque disponível.");
            }

            product.setQuantity(product.getQuantity() - saleProduct.getQuantity());
            productRepository.save(product);
        }
    }

    public CreateResponseDTO deleteSale(Long idSale) {

        Sale saleByID = saleRepository.findById(idSale)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada", "id", idSale));

        saleRepository.delete(saleByID);
        return new CreateResponseDTO(saleByID.getId().toString(), "Venda deletada com sucesso.");
    }

    private SaleDTO mapToSaleDTO(Sale sale) {
        SaleDTO saleDto = new SaleDTO();
        saleDto.setDate(sale.getDate());
        saleDto.setIdUser(sale.getIdUser());
        saleDto.setId(sale.getId());
        saleDto.setPaymentMethod(sale.getPaymentMethod());
        return saleDto;
    }

    private SaleProduct mapToSaleProduct(SaleProductDTO sale, Long saleId) {
        SaleProductId saleProductId = new SaleProductId(saleId, sale.getIdProduct());
        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setId(saleProductId);
        saleProduct.setQuantity(sale.getQuantity());
        saleProduct.setValue(sale.getValue());
        return saleProduct;
    }

    public SaleProductDTO mapToSaleProductDTO(SaleProduct sale) {
        SaleProductDTO dto = new SaleProductDTO();
        dto.setIdProduct(sale.getId().getIdProduct());
        dto.setQuantity(sale.getQuantity());
        dto.setValue(sale.getValue());
        return dto;
    }
}
