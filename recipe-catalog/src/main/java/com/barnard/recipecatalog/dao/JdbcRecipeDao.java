package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Recipe;

import java.util.List;

public class JdbcRecipeDao implements RecipeDao {
    @Override
    public Recipe getRecipe(int recipeId) {
        return null;
    }

    @Override
    public List<Recipe> searchLikeRecipes(String search, int userId) {
        return null;
    }

    @Override
    public List<Recipe> getRecipesByUserId(int userId) {
        return null;
    }

    @Override
    public List<Recipe> getRecipesByCategoryId(int categoryId) {
        return null;
    }

    @Override
    public Recipe getRecipeByMealId(int mealId) {
        return null;
    }

    @Override
    public List<Recipe> getRecipesByTagId(int tagId) {
        return null;
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return null;
    }

    @Override
    public int deleteRecipeById(int recipeId) {
        return 0;
    }
}
