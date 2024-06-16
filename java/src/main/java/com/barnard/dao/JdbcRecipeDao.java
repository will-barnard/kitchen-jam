package com.barnard.dao;


import com.barnard.exception.DaoException;
import com.barnard.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcRecipeDao implements RecipeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Recipe getRecipe(int recipeId) {

        Recipe recipe = null;
        String sql = "SELECT * " +
                "FROM recipe " +
                "WHERE recipe_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);

            if (rowSet.next()) {
                recipe = mapRowToRecipe(rowSet);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipe;
    }

    @Override
    public List<Recipe> searchLikeRecipes(String search, int userId) {

        search = "%" + search + "%";
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM recipe " +
                "WHERE recipe_name LIKE ? " +
                "AND user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipes;
    }

    @Override
    public List<Recipe> getRecipesByUserId(int userId) {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM recipe " +
                "WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipes;
    }

    @Override
    public List<Recipe> getRecipesByCategoryId(int categoryId, int userId) {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM recipe " +
                "JOIN recipe_category on recipe.recipe_id = recipe_category.recipe_id " +
                "WHERE recipe_category.category_id = ? " +
                "AND recipe.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, categoryId, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipes;

    }

    @Override
    public Recipe getRecipeByMealId(int mealId) {

        Recipe recipe = null;
        String sql = "SELECT * " +
                "FROM recipe " +
                "JOIN meal on recipe.recipe_id = meal.recipe_id " +
                "WHERE meal.meal_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);

            if (rowSet.next()) {
                recipe = mapRowToRecipe(rowSet);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipe;
    }

    @Override
    public Recipe createRecipe(Recipe recipe) {

        String sql = "INSERT INTO recipe (user_id, recipe_name, avg_cook_time, description, is_public) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING recipe_id;";

        try {
            int recipeId = jdbcTemplate.queryForObject(sql, int.class, recipe.getUserId(), recipe.getRecipeName(), recipe.getAvgCookTime(), recipe.getDescription(), recipe.isPublic());
            recipe.setRecipeId(recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return recipe;

    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        Recipe newRecipe = null;

        String sql = "UPDATE recipe SET " +
                "recipe_name = ?, avg_cook_time = ?, description = ?, is_public =? " +
                "WHERE recipe_id = ?;";

        try {

            int rowsAffected = jdbcTemplate.update(sql, recipe.getRecipeName(), recipe.getAvgCookTime(), recipe.getDescription(), recipe.isPublic(), recipe.getRecipeId());
            if (rowsAffected == 0) {
                throw new DaoException();
            }
            newRecipe = getRecipe(recipe.getRecipeId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newRecipe;
    }

    @Override
    public void deleteRecipeById(int recipeId) {

        String sql = "DELETE FROM recipe_category " +
                "WHERE recipe_id = ?;";
        String sql2 = "DELETE FROM recipe " +
                "WHERE recipe_id = ?;";

        try {

            jdbcTemplate.update(sql, recipeId);
            int rowsAffected = jdbcTemplate.update(sql2, recipeId);
            if (rowsAffected != 1) {
                throw new DaoException();
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public boolean verifyRecipeOwner(int userId, int recipeId) {

        String sql = "SELECT user_id FROM recipe " +
                "WHERE recipe_id = ?;";

        try {

            int result = jdbcTemplate.queryForObject(sql, int.class, recipeId);
            if (result != userId) {
                return false;
            } else {
                return true;
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Recipe mapRowToRecipe(SqlRowSet rs) {
        Recipe recipe = new Recipe();

        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setUserId(rs.getInt("user_id"));
        recipe.setRecipeName(rs.getString("recipe_name"));
        recipe.setAvgCookTime(rs.getInt("avg_cook_time"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImage_id(rs.getInt("image_id"));
        recipe.setPublic(rs.getBoolean("is_public"));

        return recipe;
    }
}
