package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Meal;
import com.barnard.recipecatalog.model.Recipe;

import java.util.List;

public interface RecipeDao {

    Recipe getRecipe(int recipeId);
    List<Recipe> searchLikeRecipes(String search, int userId);
    List<Recipe> getRecipesByUserId(int userId);
    List<Recipe> getRecipesByCategoryId(int categoryId);
    Recipe getRecipeByMealId(int mealId);
    List<Recipe> getRecipesByTagId(int tagId);
    Recipe createRecipe(Recipe recipe);
    Recipe updateRecipe(Recipe recipe);
    int deleteRecipeById(int recipeId);

}
