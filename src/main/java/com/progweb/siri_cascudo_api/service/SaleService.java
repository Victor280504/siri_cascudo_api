package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Sale.CreateSaleDTO;
import com.progweb.siri_cascudo_api.dto.Sale.SaleDTO;
import com.progweb.siri_cascudo_api.dto.Sale.SaleProductDTO;
import com.progweb.siri_cascudo_api.dto.Sale.UpdateSaleDTO;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.exception.ResourceNotFoundException;
import com.progweb.siri_cascudo_api.model.*;
import com.progweb.siri_cascudo_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Verifica se o produto existe
        userRepository.findById(dto.getIdUser())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Venda não encontrada", "id", dto.getIdUser()));

        // Cria e salva a nova receita
        Sale newSale = new Sale();
        Date currentDate = new Date();
        newSale.setDate(currentDate);
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

    public UpdateResponseDTO<UpdateSaleDTO> updateSale(UpdateSaleDTO dto, Long saleId) {

        Sale saleById = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada", "id", saleId));

        if (!Objects.equals(dto.getIdUser(), saleById.getIdUser()) && dto.getIdUser() != null) {
            saleById.setIdUser(dto.getIdUser());
        }
        if (dto.getDate() != saleById.getDate() && dto.getDate() != null) {
            saleById.setDate(dto.getDate());
        }
        if (!Objects.equals(dto.getPaymentMethod(), saleById.getPaymentMethod()) && dto.getPaymentMethod() != null) {
            saleById.setPaymentMethod(dto.getPaymentMethod());
        }

        List<SaleProduct> productsBySale = saleProductRepository.findById_IdSale(saleById.getId());

        if (dto.getProducts() != null) {
            List<SaleProduct> newProducts = getSaleProductsByItems(dto.getProducts(), saleById.getId());

            // Remover produtos que não estão na nova lista de produtos
            productsBySale.removeIf(product -> !newProducts.contains(product));

            // Adicionar produtos novos que não estão na lista atual
            for (SaleProduct product : newProducts) {
                if (!productsBySale.contains(product)) {
                    productsBySale.add(product);
                }
            }

            saleProductRepository.saveAll(productsBySale);

            // Remover do repositório os produtos que foram removidos da lista
            for (SaleProduct product : productsBySale) {
                if (!newProducts.contains(product)) {
                    saleProductRepository.delete(product);
                }
            }
        }

        Sale result = saleRepository.save(saleById);

        UpdateSaleDTO response = new UpdateSaleDTO();
        response.setIdUser(result.getIdUser());
        response.setDate(result.getDate());
        response.setPaymentMethod(result.getPaymentMethod());
        response.setProducts(productsBySale.stream().map(this::mapToSaleProductDTO).collect(Collectors.toList()));

        return new UpdateResponseDTO<>(saleId.toString(), "Venda atualizada com sucesso.", response);
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

    private SaleProductDTO mapToSaleProductDTO(SaleProduct sale) {
        SaleProductDTO dto = new SaleProductDTO();
        dto.setIdProduct(sale.getId().getIdProduct());
        dto.setQuantity(sale.getQuantity());
        dto.setValue(sale.getValue());
        return dto;
    }
}
