package com.wiser.consulting.reporting_service.dto;

import java.util.List;

import com.wiser.consulting.reporting_service.entity.OrderReport;

public record Report(List<OrderReport> orderList, String dateRange, int totalOrder, Double totalOderAmount) {
} 