package com.progweb.siri_cascudo_api.service;

import com.progweb.siri_cascudo_api.dto.Sale.*;
import com.progweb.siri_cascudo_api.model.Sale;
import com.progweb.siri_cascudo_api.repository.IngredientRepository;
import com.progweb.siri_cascudo_api.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {
    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleService saleService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeService recipeService;


    public ReportDTO getReport() {
        ReportDTO report = new ReportDTO();
        report.setMonthlyReport(getMonthlyReport());
        report.setWeeklyReport(getWeeklyReport());
        return report;
    }

    public List<ReportSaleData> getWeeklyReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        return generateReport(startOfWeek, endOfWeek);
    }

    public List<ReportSaleData> getMonthlyReport() {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth());

        return generateReport(startOfMonth, endOfMonth);
    }

    private List<ReportSaleData> generateReport(LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Sale> sales = saleRepository.findByDateBetween(start, end);

        List<ReportSaleData> reportDataList = sales.stream().map(sale -> {
            BigDecimal revenue = calculateRevenue(sale);
            BigDecimal expenses = calculateExpenses(sale);
            BigDecimal profit = revenue.subtract(expenses);

            ReportSaleData reportData = new ReportSaleData();
            reportData.setDate(sale.getDate().toString());
            reportData.setTotalOrders(1); // cada venda representa um pedido
            reportData.setRevenue(revenue.doubleValue());
            reportData.setExpenses(expenses.doubleValue());
            reportData.setProfit(profit.doubleValue());
            return reportData;
        }).collect(Collectors.toList());

        calculateComparison(reportDataList);

        return reportDataList;
    }

    private void calculateComparison(List<ReportSaleData> reportDataList) {
        for (int i = 1; i < reportDataList.size(); i++) {
            double previousProfit = reportDataList.get(i - 1).getProfit();
            double currentProfit = reportDataList.get(i).getProfit();
            double comparison = currentProfit - previousProfit;
            reportDataList.get(i).setComparison(comparison);
        }
    }

    private BigDecimal calculateRevenue(Sale sale) {
        return BigDecimal.valueOf(saleService.getTotalFromSale(sale.getId()));
    }

    // falta implementar
    private BigDecimal calculateExpenses(Sale sale) {
        SaleDetailsDTO saleDetailsDTO = saleService.getSaleWithDetailsBySaleId(sale.getId());
        double result = saleDetailsDTO.getProducts()
                .stream()
                .mapToDouble(SaleProductWithProductDTO -> recipeService.getRecipeCostByProduct(SaleProductWithProductDTO.getProduct().getId()) * SaleProductWithProductDTO.getQuantity())
                .sum();
        return BigDecimal.valueOf(result);
    }
}