package com.barnard.dao;


import com.barnard.model.Meal;

import java.util.List;

public interface MealDao {

    Meal getMeal(int mealId);
    List<Meal> searchLikeMeals(String search);
    List<Meal> getMealsByTag(int tagId);
    List<Meal> getMealsByUserId(int userId);
    List<Meal> getMealsByRecipeId(int recipeId);
    int createMeal(Meal meal);
    Meal updateMeal(Meal meal);
    int deleteMealById(int mealId);

}
