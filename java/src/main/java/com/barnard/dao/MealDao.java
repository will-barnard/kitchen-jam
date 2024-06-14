package com.barnard.dao;


import com.barnard.model.Meal;

import java.util.List;

public interface MealDao {

    Meal getMeal(int mealId);
    List<Meal> searchLikeMeals(String search, int userId);
    List<Meal> getMealsByTagAndUser(int tagId, int userId);
    List<Meal> getMealsByUserId(int userId);
    List<Meal> getMealsByRecipeId(int recipeId);
    Meal createMeal(Meal meal);
    Meal updateMeal(Meal meal);
    void deleteMealById(int mealId);
    boolean verifyMealOwner(int userId, int mealId);

}
