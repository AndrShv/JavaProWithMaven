package com.example.service;

import com.example.model.Electronics;
import com.example.repository.ComponentRepository;
import com.example.repository.ElectronicsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ElectronicsService {

    private final ElectronicsRepository electronicsRepository;
    private final ComponentRepository componentRepository;

    @Autowired
    public ElectronicsService(ElectronicsRepository electronicsRepository, ComponentRepository componentRepository) {
        this.electronicsRepository = electronicsRepository;
        this.componentRepository = componentRepository;
    }

    public Electronics getElectronicsById(int id) {
        Optional<Electronics> optionalElectronics = electronicsRepository.findById((long) id);
        return optionalElectronics.orElse(null);
    }

    public Electronics createElectronics(Electronics electronics) {
        return electronicsRepository.save(electronics);
    }

    public boolean deleteElectronics(int id) {
        Optional<Electronics> optionalElectronics = electronicsRepository.findById((long) id);
        if (optionalElectronics.isPresent()) {
            electronicsRepository.delete(optionalElectronics.get());
            return true;
        }
        return false;
    }
}
