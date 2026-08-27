package com.ironempire.repository;

import com.ironempire.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPlanRepository extends JpaRepository<Plan, Long> {

}
