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

@Component
public class JdbcIngredientDao implements IngredientDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Ingredient> getIngredientsByMeal(int mealId) {

        // todo unimplemented via db refactor

        try {


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public List<Ingredient> getIngredientsByRecipe(int recipeId) {

        // todo unimplemented via db refactor

        try {


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public List<Meal> getIngredientsByMeals(List<Meal> meals) {

        // todo this is a whole thing
        String sql = "";
        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public List<Recipe> getIngredientsByRecipes(List<Recipe> recipes) {

        // todo this is a whole thing
        String sql = "SELECT * from ingredients ";
        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Ingredient addIngredientsToMeal(Ingredient ingredient) {

        // todo unimplemented via db refactor

        try {


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public Ingredient addIngredientsToRecipe(Ingredient ingredient) {

        // todo unimplemented via db refactor

        try {


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;
    }

    @Override
    public void deleteAllIngredientsFromMeal(int mealId) {

        // todo unimplemented via db refactor

        try {


        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteAllIngredientsFromRecipe(int recipeId) {

        // todo unimplemented via db refactor

        try {


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
