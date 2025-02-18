package com.example.repository;

import com.example.model.User.User;
import com.example.model.VirtualCurrency;
import jakarta.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByCurrency(VirtualCurrency currency);
    <S extends Transaction> S save(S transaction);
    void deleteById(Long id);
}
