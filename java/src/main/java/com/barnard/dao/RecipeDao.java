package com.barnard.dao;


import com.barnard.model.Friend;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;

import java.util.List;

public interface RecipeDao {

    Recipe getRecipe(int recipeId);
    List<Recipe> searchLikeRecipes(String search, int userId);
    List<Recipe> getRecipesByUserId(int userId);
    List<Recipe> getRecipesByCategoryId(int categoryId, int userId);
    List<Recipe> getUserProfileRecipes(int userId);
    List<Recipe> getRecipeFeed(int userId, List<Friend> friendsList);
    List<Recipe> getMoreRecipes(int userId, List<Friend> friendsList, int timesLoaded);
    Recipe getRecipeByMealId(int mealId);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    Recipe getPublicRecipe(String uuid);
    String makePublic(Recipe recipe);
    void makePrivate(Recipe recipe);
    boolean isRecipePublic(int recipeId);
    void deleteRecipeById(int recipeId);
    boolean verifyRecipeOwner(int userId, int recipeId);

}
