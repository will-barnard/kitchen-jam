package com.barnard.dao;

import com.barnard.model.Ingredient;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface IngredientDao {

    Ingredient getIngredient(int ingredientId);
    List<Ingredient> getIngredientsByMeal(int mealId);
    List<Ingredient> getIngredientsByRecipe(int recipeId);
    Ingredient createIngredient(Ingredient ingredient);
    Ingredient updateIngredient(Ingredient ingredient);
    void deleteIngredient(int ingredientId);
}
