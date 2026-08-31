package com.ironempire.repository.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ironempire.model.Pago;

@Repository
public interface JpaPagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {
}