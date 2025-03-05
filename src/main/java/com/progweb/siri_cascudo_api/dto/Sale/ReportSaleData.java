package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

@Data
public class ReportSaleData {
    private String date;
    private int totalOrders;
    private double revenue;
    private double expenses;
    private double profit;
    private double comparison;
}