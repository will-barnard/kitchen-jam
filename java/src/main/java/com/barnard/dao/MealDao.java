package com.barnard.dao;


import com.barnard.model.Meal;

import java.util.List;

public interface MealDao {

    Meal getMeal(int mealId);
    List<Meal> searchLikeMeals(String search, int userId);
    List<Meal> getMealsByTag(int tagId);
    List<Meal> getMealsByUserId(int userId);
    List<Meal> getMealsByRecipeId(int recipeId);
    Meal createMeal(Meal meal);
    Meal updateMeal(Meal meal);
    void deleteMealById(int mealId);

}
