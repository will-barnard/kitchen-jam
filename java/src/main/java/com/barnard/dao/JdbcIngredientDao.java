package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Ingredient;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import com.barnard.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

@Component
public class JdbcIngredientDao implements IngredientDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Ingredient> getIngredientsByMeal(int mealId) {

        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * from ingredient " +
                "WHERE meal_id = ? " +
                "ORDER BY list_order ASC;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);
            while (rowSet.next()) {
                ingredients.add(mapRowToIngredient(rowSet));
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredients;
    }

    @Override
    public List<Ingredient> getIngredientsByRecipe(int recipeId) {

        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * from ingredient " +
                "WHERE recipe_id = ? " +
                "ORDER BY list_order ASC;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                ingredients.add(mapRowToIngredient(rowSet));
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredients;
    }

    @Override
    public List<Meal> getIngredientsByMeals(List<Meal> meals) {

        String sql = "SELECT * from ingredient " +
                "WHERE meal_id = ";

        for (int i = 0; i < meals.size(); i++) {
            if (i == 0) {
                sql += meals.get(i).getMealId() + " ";
            } else {
                sql += "OR meal_id = " + meals.get(i).getMealId() + " ";
            }
        }

        sql += "ORDER BY meal_id, list_order ASC;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            List<Ingredient> tempList = new ArrayList<>();
            int mealId = 0;
            boolean isResult = false;
            while (rowSet.next()) {
                isResult = true;
                Ingredient ingredient = mapRowToIngredient(rowSet);
                if (mealId == 0) {
                    tempList.add(ingredient);
                    mealId = ingredient.getMealId();
                } else if (ingredient.getMealId() == mealId){
                    tempList.add(ingredient);
                } else {
                    int finalMealId = mealId;
                    OptionalInt index = meals.stream()
                            .filter(meal -> meal.getMealId() == finalMealId)
                            .mapToInt(meals::indexOf)
                            .findFirst();
                    meals.get(index.getAsInt()).setIngredientList(tempList);
                    // reset values
                    tempList = new ArrayList<>();
                    tempList.add(ingredient);
                    mealId = ingredient.getMealId();
                }
            }
            if (isResult) {
                int finalMealId = mealId;
                OptionalInt index = meals.stream()
                        .filter(meal -> meal.getMealId() == finalMealId)
                        .mapToInt(meals::indexOf)
                        .findFirst();
                meals.get(index.getAsInt()).setIngredientList(tempList);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return meals;
    }

    @Override
    public List<Recipe> getIngredientsByRecipes(List<Recipe> recipes) {

        String sql = "SELECT * from ingredient " +
                "WHERE recipe_id = ";

        for (int i = 0; i < recipes.size(); i++) {
            if (i == 0) {
                sql += recipes.get(i).getRecipeId() + " ";
            } else {
                sql += "OR recipe_id = " + recipes.get(i).getRecipeId() + " ";
            }
        }

        sql += "ORDER BY recipe_id, list_order ASC;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
            List<Ingredient> tempList = new ArrayList<>();
            int recipeId = 0;
            boolean isResult = false;
            while (rowSet.next()) {
                isResult = true;
                Ingredient ingredient = mapRowToIngredient(rowSet);
                if (recipeId == 0) {
                    tempList.add(ingredient);
                    recipeId = ingredient.getRecipeId();
                } else if (ingredient.getRecipeId() == recipeId){
                    tempList.add(ingredient);
                } else {
                    int finalRecipeId = recipeId;
                    OptionalInt index = recipes.stream()
                            .filter(recipe -> recipe.getRecipeId() == finalRecipeId)
                            .mapToInt(recipes::indexOf)
                            .findFirst();
                    recipes.get(index.getAsInt()).setIngredientList(tempList);
                    // reset values
                    tempList = new ArrayList<>();
                    tempList.add(ingredient);
                    recipeId = ingredient.getRecipeId();
                }
            }
            if (isResult) {
                int finalRecipeId = recipeId;
                OptionalInt index = recipes.stream()
                        .filter(recipe -> recipe.getRecipeId() == finalRecipeId)
                        .mapToInt(recipes::indexOf)
                        .findFirst();
                recipes.get(index.getAsInt()).setIngredientList(tempList);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return recipes;
    }

    @Override
    public Meal addIngredientsToMeal(List<Ingredient> ingredients, Meal meal) {

        String sql = "INSERT INTO ingredient (meal_id, user_id, quantity, ingredient_name, list_order) " +
                "VALUES (?, ?, ?, ?, ?);";

        try {
            for (Ingredient ingredient : ingredients) {
                jdbcTemplate.update(sql, meal.getMealId(), meal.getUserId(), ingredient.getQuantity(), ingredient.getIngredientName(), ingredient.getListOrder());
            }
            meal.setIngredientList(getIngredientsByMeal(meal.getMealId()));
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return meal;
    }

    @Override
    public Recipe addIngredientsToRecipe(List<Ingredient> ingredients, Recipe recipe) {

        String sql = "INSERT INTO ingredient (recipe_id, user_id, quantity, ingredient_name, list_order) " +
                "VALUES (?, ?, ?, ?, ?);";

        try {
            for (Ingredient ingredient : ingredients) {
                jdbcTemplate.update(sql, recipe.getRecipeId(), recipe.getUserId(), ingredient.getQuantity(), ingredient.getIngredientName(), ingredient.getListOrder());
            }
            recipe.setIngredientList(getIngredientsByRecipe(recipe.getRecipeId()));
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return recipe;
    }

    @Override
    public void deleteAllIngredientsFromMeal(int mealId) {

        String sql = "DELETE FROM ingredient " +
                "WHERE meal_id = ?;";

        try {
            jdbcTemplate.update(sql, mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteAllIngredientsFromRecipe(int recipeId) {

        String sql = "DELETE FROM ingredient " +
                "WHERE recipe_id = ?;";

        try {
            jdbcTemplate.update(sql, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }



    private Ingredient mapRowToIngredient(SqlRowSet rs) {
        Ingredient ingredient = new Ingredient();

        ingredient.setIngredientId(rs.getInt("ingredient_id"));
        ingredient.setUserId(rs.getInt("user_id"));
        ingredient.setIngredientName(rs.getString("ingredient_name"));
        if (rs.getObject("quantity") != null) {
            ingredient.setQuantity(rs.getString("quantity"));
        }
        if (rs.getObject("list_order") != null) {
            ingredient.setListOrder(rs.getInt("list_order"));
        }
        if (rs.getObject("meal_id") != null) {
            ingredient.setMealId(rs.getInt("meal_id"));
        }
        if (rs.getObject("recipe_id") != null) {
            ingredient.setRecipeId(rs.getInt("recipe_id"));
        }

        return ingredient;
    }
}
