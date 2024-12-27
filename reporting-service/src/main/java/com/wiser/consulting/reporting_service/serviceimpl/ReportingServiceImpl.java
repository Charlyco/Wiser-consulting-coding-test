package com.wiser.consulting.reporting_service.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.wiser.consulting.reporting_service.dto.Report;
import com.wiser.consulting.reporting_service.entity.OrderReport;
import com.wiser.consulting.reporting_service.repo.OrderReportRepository;
import com.wiser.consulting.reporting_service.service.ReportingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService{
    private final OrderReportRepository orderReportRepo;
    
    @Override
    public void SaveReport(OrderReport report) {
        orderReportRepo.save(report);
    }

    @Override
    public Report filterOrderByDate(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        List<OrderReport> orderReports = new ArrayList<>();

        orderReports = orderReportRepo.findAll().stream()
        .filter(order -> {
            LocalDate orderDate = LocalDate.parse(order.getOrderDateTime());
            return (orderDate.isEqual(start) || orderDate.isAfter(start)) &&
                   (orderDate.isEqual(end) || orderDate.isBefore(end));
        })
        .collect(Collectors.toList());

        Double totalOrderAmount = 0.0;
        for(OrderReport order : orderReports) {
            totalOrderAmount += order.getTotalCost();
        }

        return new Report(orderReports, startDate + " - "+ endDate, orderReports.size(), totalOrderAmount);
    }   

}
