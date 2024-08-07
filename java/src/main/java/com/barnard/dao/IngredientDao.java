package com.barnard.dao;

import com.barnard.model.Ingredient;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface IngredientDao {

    Ingredient getIngredient(int ingredientId);
    Ingredient getIngredientFromMeal(int ingredientId, int mealId);
    Ingredient getIngredientFromRecipe(int ingredientId, int recipeId);
    List<Ingredient> searchLikeIngredients(String search, int userId);
    List<Ingredient> getIngredientsByMeal(int mealId);
    List<Ingredient> getIngredientsByRecipe(int recipeId);
    Ingredient createIngredient(Ingredient ingredient);
    Ingredient updateIngredient(Ingredient ingredient);
    Ingredient addIngredientToMeal(Ingredient ingredient);
    Ingredient addIngredientToRecipe(Ingredient ingredient);
    void deleteAllIngredientsFromMeal(int mealId);
    void deleteAllIngredientsFromRecipe(int recipeId);
    void deleteIngredient(int ingredientId);
}
