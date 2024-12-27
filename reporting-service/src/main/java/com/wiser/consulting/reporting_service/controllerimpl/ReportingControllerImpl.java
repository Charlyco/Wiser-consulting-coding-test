package com.wiser.consulting.reporting_service.controllerimpl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.wiser.consulting.reporting_service.controller.ReportingController;
import com.wiser.consulting.reporting_service.dto.Report;
import com.wiser.consulting.reporting_service.entity.OrderReport;
import com.wiser.consulting.reporting_service.service.ReportingService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportingControllerImpl implements ReportingController{
    private final ReportingService reportingService;

    @Override
    public void SaveReport(OrderReport report) {
        reportingService.SaveReport(report);
    }

    @Override
    public ResponseEntity<Report> filterOrderByDate(String startDate, String endDate) {
        return ResponseEntity.ok(reportingService.filterOrderByDate(startDate, endDate));
    }

}
