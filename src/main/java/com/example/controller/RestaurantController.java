package com.example.controller;

import com.example.repository.RestaurantRepository;
import com.example.service.RestaurantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final RestaurantRepository  restaurantRepository;

    public RestaurantController(RestaurantService restaurantService, RestaurantRepository restaurantRepository) {
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }
}
