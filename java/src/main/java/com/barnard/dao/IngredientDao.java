package com.barnard.dao;

import com.barnard.model.Ingredient;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface IngredientDao {


    List<Ingredient> getIngredientsByMeal(int mealId);
    List<Ingredient> getIngredientsByRecipe(int recipeId);
    List<Meal> getIngredientsByMeals(List<Meal> meals);
    List<Recipe> getIngredientsByRecipes(List<Recipe> recipes);
    Meal addIngredientsToMeal(List<Ingredient> ingredients, Meal meal);
    Recipe addIngredientsToRecipe(List<Ingredient> ingredients, Recipe recipe);
    void deleteAllIngredientsFromMeal(int mealId);
    void deleteAllIngredientsFromRecipe(int recipeId);

}
