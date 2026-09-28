package com.ironempire.service.pago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ironempire.dto.response.pago.InformacionFinancieraResponse;
import com.ironempire.model.Pago;
import com.ironempire.repository.JpaPagoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultarInformacionFinancieraService {
    
    private final JpaPagoRepository jpaPagoRepository;

    @Transactional(readOnly = true)
    public InformacionFinancieraResponse consultarInformacionFinanciera(LocalDate desde, LocalDate hasta) {

        List<Pago> pagosDelPeriodo = jpaPagoRepository.findByFechaPagoBetween(desde, hasta);

        BigDecimal ingresosTotales = pagosDelPeriodo.stream()
                .map(Pago::getMontoAbonado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> ingresosPorPlan = pagosDelPeriodo.stream()
                .collect(Collectors.groupingBy(
                    pago -> pago.getPlan().getNombre(),
                    LinkedHashMap::new,
                    Collectors.reducing(BigDecimal.ZERO, Pago::getMontoAbonado,BigDecimal::add)));

        return new InformacionFinancieraResponse(desde, hasta, ingresosTotales, pagosDelPeriodo.size(), ingresosPorPlan);

}
