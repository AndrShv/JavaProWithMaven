package com.example.repository;

import com.example.model.Electronics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectronicsRepository extends JpaRepository<Electronics, Long>{
}
