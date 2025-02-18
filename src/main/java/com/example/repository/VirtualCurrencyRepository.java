package com.example.repository;

import com.example.model.VirtualCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VirtualCurrencyRepository extends JpaRepository<VirtualCurrency, Long> {
    Optional<VirtualCurrency> findByCurrencyName(String currencyName);
    <S extends VirtualCurrency> S save(S currency);
    void deleteById(Long id);
}
