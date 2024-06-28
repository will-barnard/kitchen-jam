package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Ingredient;
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
    public Ingredient getIngredient(int ingredientId) {
        Ingredient ingredient = null;

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredient;
    }

    @Override
    public List<Ingredient> getIngredientsByMeal(int mealId) {
        List<Ingredient> ingredientList = new ArrayList<>();

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredientList;
    }

    @Override
    public List<Ingredient> getIngredientsByRecipe(int recipeId) {
        List<Ingredient> ingredientList = new ArrayList<>();

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredientList;
    }

    @Override
    public Ingredient createIngredient(Ingredient ingredient) {
        Ingredient newIngredient = null;

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newIngredient;
    }

    @Override
    public Ingredient updateIngredient(Ingredient ingredient) {
        Ingredient updatedIngredient = null;

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedIngredient;
    }

    @Override
    public void deleteIngredient(int ingredientId) {

        try {

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Ingredient mapRowToIngredient(SqlRowSet rs, boolean isMeal) {
        Ingredient ingredient = new Ingredient();

        ingredient.setIngredientId(rs.getInt("ingredient_id"));
        ingredient.setUserId(rs.getInt("user_id"));
        ingredient.setIngredientName(rs.getString("ingredient_name"));
        ingredient.setQuantity(rs.getString("quantity"));
        ingredient.setListOrder(rs.getInt("list_order"));
        if (isMeal) {
            ingredient.setMealId(rs.getInt("meal_id"));
        } else {
            ingredient.setRecipeId(rs.getInt("recipe_id"));
        }

        return ingredient;
    }
}
