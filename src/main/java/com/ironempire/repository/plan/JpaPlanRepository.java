package com.ironempire.repository.plan;

import org.hibernate.Remove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ironempire.model.Plan;

@Repository
public interface JpaPlanRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {
}
