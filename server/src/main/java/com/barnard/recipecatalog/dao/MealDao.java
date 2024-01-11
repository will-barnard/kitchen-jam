package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Meal;
import com.barnard.recipecatalog.model.Tag;

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
