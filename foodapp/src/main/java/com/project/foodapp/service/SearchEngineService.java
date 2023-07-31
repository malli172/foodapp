package com.project.foodapp.service;

import com.project.foodapp.model.Dish;
import com.project.foodapp.model.RestaurantInfo;
import java.util.List;


public interface SearchEngineService {
    void addRestaurant(String word, String restaurantId);
    List<String> searchRestaurant(String word);
    void removeRestaurant(String word, String restaurantId);
    void eraseInfo(RestaurantInfo info, String restaurantId);
    void eraseDishes(List<Dish> dishes, String restaurantId);
    void updateInfo(RestaurantInfo info, String restaurantId);
}
