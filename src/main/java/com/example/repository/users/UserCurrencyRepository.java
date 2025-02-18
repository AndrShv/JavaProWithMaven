package com.example.repository.users;

import com.example.model.User.User;
import com.example.model.User.UserCurrency;
import com.example.model.VirtualCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCurrencyRepository extends JpaRepository<UserCurrency, Long> {
    Optional<UserCurrency> findByUserAndCurrency(User user, VirtualCurrency currency);
    List<UserCurrency> findByUser(User user);
    <S extends UserCurrency> S save(S userCurrency);
    void deleteById(Long id);
}
