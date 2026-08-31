package com.ironempire.repository.rutina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ironempire.model.Rutina;

@Repository
public interface JpaRutinaRepository extends JpaRepository<Rutina, Long>, JpaSpecificationExecutor<Rutina> {
}
