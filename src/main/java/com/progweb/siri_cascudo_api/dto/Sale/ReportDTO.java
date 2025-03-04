package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;
import java.util.List;

public class ReportDTO 
{
    private List<ReportSaleData> weeklyReport;
    private List<ReportSaleData> monthlyReport;

    public void setWeeklyReport(List<ReportSaleData> weeklyReport) {
        this.weeklyReport = weeklyReport;
    }

    public void setMonthlyReport(List<ReportSaleData> monthlyReport) {
        this.monthlyReport = monthlyReport;
    }
    
    @Data
    public static class ReportSaleData {
        private String date;
        private int totalOrders;
        private double revenue;
        private double expenses;
        private double profit;
        private double comparison;
    }
}