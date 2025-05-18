package com.example.aibilling.repository;

import com.example.aibilling.entity.BillingDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingDepartmentRepository extends JpaRepository<BillingDepartment, Long> {
}
