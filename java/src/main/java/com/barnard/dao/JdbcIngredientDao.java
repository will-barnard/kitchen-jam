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
        String sql = "SELECT * FROM ingredient " +
                "WHERE ingredient_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, ingredientId);
            if (rowSet.next()) {
                ingredient = mapRowToIngredient(rowSet);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredient;
    }

    @Override
    public Ingredient getIngredientFromMeal(int ingredientId, int mealId) {
        Ingredient ingredient = null;
        String sql = "SELECT * FROM ingredient " +
                "JOIN ingredient_meal ON ingredient.ingredient_id = ingredient_meal.meal_id " +
                "WHERE ingredient.ingredient_id = ? " +
                "AND ingredient_meal.meal_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, ingredientId, mealId);
            if (rowSet.next()) {
                ingredient = mapRowToIngredient(rowSet);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return ingredient;
    }

    @Override
    public Ingredient getIngredientFromRecipe(int ingredientId, int recipeId) {
        Ingredient ingredient = null;
        String sql = "SELECT * FROM ingredient " +
                "JOIN ingredient_meal ON ingredient.ingredient_id = ingredient_recipe.recipe_id " +
                "WHERE ingredient.ingredient_id = ? " +
                "AND ingredient_recipe.recipe_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, ingredientId, recipeId);
            if (rowSet.next()) {
                ingredient = mapRowToIngredient(rowSet);
            }

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
        String sql = "SELECT * FROM ingredient " +
                "JOIN ingredient_meal ON ingredient.ingredient_id = ingredient_meal.meal_id " +
                "WHERE ingredient_meal.meal_id = ? " +
                "ORDER BY ingredient_meal.list_order ASC;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);
            while (rowSet.next()) {
                ingredientList.add(mapRowToIngredient(rowSet));
            }

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
        String sql = "SELECT * FROM ingredient " +
                "JOIN ingredient_recipe ON ingredient.ingredient_id = ingredient_recipe.recipe_id " +
                "WHERE ingredient_recipe.recipe_id = ? " +
                "ORDER BY ingredient_recipe.list_order ASC;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                ingredientList.add(mapRowToIngredient(rowSet));
            }

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
        String sql = "INSERT INTO ingredient (user_id, ingredient_name) " +
                "VALUES (?, ?) " +
                "RETURNING ingredient_id";

        try {

            int ingredientId = jdbcTemplate.queryForObject(sql, int.class, ingredient.getUserId(), ingredient.getIngredientName());
            newIngredient = getIngredient(ingredientId);

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
        String sql = "UPDATE ingredient SET ingredient_name = ? " +
                "WHERE ingredient_id = ?;";

        try {

            int rowsAffected = jdbcTemplate.update(sql, ingredient.getIngredientName(), ingredient.getIngredientId());
            if (rowsAffected != 1) {
                throw new DataIntegrityViolationException("No rows affected");
            }
            updatedIngredient = getIngredient(ingredient.getIngredientId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedIngredient;
    }

    @Override
    public Ingredient addIngredientToMeal(Ingredient ingredient) {
        Ingredient updatedIngredient = null;
        String sql = "INSERT INTO ingredient_meal (ingredient_id, meal_id, quantity, list_order) " +
                "VALUES (?, ?, ?, ?);";

        try {

            jdbcTemplate.update(sql, ingredient.getIngredientId(), ingredient.getMealId(), ingredient.getQuantity(), ingredient.getListOrder());
            updatedIngredient = getIngredientFromMeal(ingredient.getIngredientId(), ingredient.getMealId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedIngredient;
    }

    @Override
    public Ingredient addIngredientToRecipe(Ingredient ingredient) {
        Ingredient updatedIngredient = null;
        String sql = "INSERT INTO ingredient_recipe (ingredient_id, recipe_id, quantity, list_order) " +
                "VALUES (?, ?, ?, ?);";

        try {

            jdbcTemplate.update(sql, ingredient.getIngredientId(), ingredient.getRecipeId(), ingredient.getQuantity(), ingredient.getListOrder());
            updatedIngredient = getIngredientFromRecipe(ingredient.getIngredientId(), ingredient.getRecipeId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedIngredient;
    }

    @Override
    public void deleteIngredientFromMeal(Ingredient ingredient) {
        String sql = "DELETE FROM ingredient_meal " +
                "WHERE ingredient_id = ? " +
                "AND meal_id = ?;";

        try {

            jdbcTemplate.update(sql, ingredient.getIngredientId(), ingredient.getMealId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteIngredientFromRecipe(Ingredient ingredient) {
        String sql = "DELETE FROM ingredient_recipe " +
                "WHERE ingredient_id = ? " +
                "AND recipe_id = ?;";

        try {

            jdbcTemplate.update(sql, ingredient.getIngredientId(), ingredient.getRecipeId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteIngredient(int ingredientId) {
        String sql = "DELETE FROM ingredient_recipe " +
                "WHERE ingredient_id = ? " +
                "AND recipe_id = ?; " +
                "DELETE FROM ingredient_meal " +
                "WHERE ingredient_id = ? " +
                "AND meal_id = ?; " +
                "DELETE FROM ingredient " +
                "WHERE ingredient_id = ?;";

        try {

            jdbcTemplate.update(sql, ingredientId, ingredientId, ingredientId);

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
