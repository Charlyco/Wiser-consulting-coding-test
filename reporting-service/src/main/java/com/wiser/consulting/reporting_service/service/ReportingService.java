package com.wiser.consulting.reporting_service.service;

import org.springframework.stereotype.Service;

import com.wiser.consulting.reporting_service.dto.Report;
import com.wiser.consulting.reporting_service.entity.OrderReport;

@Service
public interface ReportingService {

    void SaveReport(OrderReport report);

    Report filterOrderByDate(String startDate, String endDate);

}
