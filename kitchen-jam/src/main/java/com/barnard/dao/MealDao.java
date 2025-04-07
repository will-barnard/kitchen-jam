package com.barnard.dao;


import com.barnard.model.Friend;
import com.barnard.model.Meal;

import java.util.List;

public interface MealDao {

    Meal getMeal(int mealId);
    List<Meal> searchLikeMeals(String search, int userId);
    List<Meal> getMealsByTagAndUser(int tagId, int userId);
    List<Meal> getMealsByUserId(int userId);
    List<Meal> getMealsByRecipeId(int recipeId);
    List<Meal> getUserProfileMeals(int userId);
    List<Meal> getMealFeed(int userId, List<Friend> friendsList);
    List<Meal> getMoreMeals(int userId, List<Friend> friendsList, int timesLoaded);
    Meal createMeal(Meal meal);
    Meal updateMeal(Meal meal);
    Meal getPublicMeal(String uuid);
    String makePublic(Meal meal);
    void makePrivate(Meal meal);
    boolean isMealPublic(int mealId);
    void deleteMealById(int mealId);
    boolean verifyMealOwner(int userId, int mealId);

}
