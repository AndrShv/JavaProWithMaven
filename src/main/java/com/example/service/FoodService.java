package com.example.service;

import com.example.model.Food;
import com.example.repository.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public Food getFoodById(Integer id) {
        Optional<Food> optionalFood = foodRepository.findById((long) id);
        return optionalFood.orElse(null);
    }
}
