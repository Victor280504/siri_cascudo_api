package com.progweb.siri_cascudo_api.controller;

import com.progweb.siri_cascudo_api.dto.CreateResponseDTO;
import com.progweb.siri_cascudo_api.dto.Sale.*;
import com.progweb.siri_cascudo_api.dto.UpdateResponseDTO;
import com.progweb.siri_cascudo_api.service.ReportService;
import com.progweb.siri_cascudo_api.service.SaleService;
import com.progweb.siri_cascudo_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @GetMapping
    public ResponseEntity<List<SaleWithTotalDTO>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/report")
    public ResponseEntity<ReportDTO> getMothlyReport() {
        return ResponseEntity.ok(reportService.getReport());
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<SaleDetailsDTO> getSaleDetailsById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleWithDetailsBySaleId(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<SaleWithTotalDTO>> getSalesByUseryId(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSalesByUseryId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UpdateSaleDTO> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }


    @PostMapping
    public ResponseEntity<CreateResponseDTO> createSale(@RequestBody CreateSaleDTO dto) {
        return ResponseEntity.ok(saleService.createSale(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateResponseDTO<UpdateSaleDTO>> updateSale(@RequestBody UpdateSaleDTO dto, @PathVariable Long id) {
        return ResponseEntity.ok(saleService.updateSale(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CreateResponseDTO> deleteSale(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.deleteSale(id));
    }
}
