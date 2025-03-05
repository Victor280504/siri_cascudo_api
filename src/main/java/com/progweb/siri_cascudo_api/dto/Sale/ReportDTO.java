package com.progweb.siri_cascudo_api.dto.Sale;

import lombok.Data;

import java.util.List;

@Data
public class ReportDTO {
    private List<ReportSaleData> weeklyReport;
    private List<ReportSaleData> monthlyReport;
}