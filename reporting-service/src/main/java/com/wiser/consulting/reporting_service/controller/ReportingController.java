package com.wiser.consulting.reporting_service.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wiser.consulting.reporting_service.config.KafkaTopic;
import com.wiser.consulting.reporting_service.dto.Report;
import com.wiser.consulting.reporting_service.entity.OrderReport;

@RestController
@RequestMapping("reporting-service/api/v1/")
public interface ReportingController {

    @KafkaListener(
        topics = KafkaTopic.PREVIOUS_ORDER,
        containerFactory = "orderReportListenerContainerFactory",
        groupId = "wiser_consulting"
    )
    void SaveReport(OrderReport report);

@GetMapping("report")
ResponseEntity<Report> filterOrderByDate(@RequestParam("startDate") String startDate,  @RequestParam("endDate") String endDate);

}
