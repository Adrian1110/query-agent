package com.aibilling.repository;

import com.aibilling.entity.BillingDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingDepartmentRepository extends JpaRepository<BillingDepartment, Long> {
}
