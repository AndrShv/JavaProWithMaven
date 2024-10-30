package com.example.repository;

import com.example.model.Electronics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectronicsRepository extends JpaRepository<Electronics, Long>{
}
