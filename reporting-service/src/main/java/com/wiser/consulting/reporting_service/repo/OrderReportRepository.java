package com.wiser.consulting.reporting_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wiser.consulting.reporting_service.entity.OrderReport;

@Repository
public interface OrderReportRepository extends JpaRepository<OrderReport, String>{

}
