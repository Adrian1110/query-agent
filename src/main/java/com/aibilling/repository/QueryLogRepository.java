package com.aibilling.repository;

import com.aibilling.entity.QueryLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryLogRepository extends JpaRepository<QueryLog, Long> {
}
