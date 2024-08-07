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
        String sql = "SELECT recipe.*, category.category_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "WHERE recipe.recipe_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);

            if (rowSet.next()) {
                recipe = mapRowToRecipe(rowSet, true);
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

        search = "%" + search.toLowerCase() + "%";
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe.*, category.category_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "WHERE LOWER (recipe.recipe_name) LIKE ? " +
                "AND recipe.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet, true));
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
        String sql = "SELECT recipe.*, category.category_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "WHERE recipe.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet, true));
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
        String sql = "SELECT recipe.*, category.category_name " +
                "FROM recipe " +
                "LEFT JOIN category on recipe.category_id = category.category_id " +
                "WHERE recipe.category_id = ? " +
                "AND recipe.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, categoryId, userId);

            while (rowSet.next()) {
                recipes.add(mapRowToRecipe(rowSet, true));
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
        String sql = "SELECT recipe.*, category.category_name " +
                "FROM recipe " +
                "JOIN meal on recipe.recipe_id = meal.recipe_id " +
                "LEFT JOIN category on recipe.category_id = category.category_id " +
                "WHERE meal.meal_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);

            if (rowSet.next()) {
                recipe = mapRowToRecipe(rowSet, true);
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

        String sql = "INSERT INTO recipe (user_id, recipe_name, description, category_id, is_public) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING recipe_id;";

        try {
            int recipeId = jdbcTemplate.queryForObject(sql, int.class, recipe.getUserId(), recipe.getRecipeName(), recipe.getDescription(), recipe.getCategoryId(), recipe.isPublic());
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
                "recipe_name = ?, description = ?, is_public = ?, category_id = ? " +
                "WHERE recipe_id = ?;";

        try {

            int rowsAffected = jdbcTemplate.update(sql, recipe.getRecipeName(), recipe.getDescription(), recipe.isPublic(), recipe.getCategoryId(), recipe.getRecipeId());
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

        String sql = "UPDATE meal SET recipe_id = NULL " +
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

    @Override
    public void aggregateRecipeData(int recipeId) {
        String sql = "SELECT cook_time FROM meal " +
                "WHERE recipe_id = ?;";
        String sql2 = "UPDATE recipe SET avg_cook_time = ? " +
                "WHERE recipe_id = ?;";
        int count = 0;
        int cookTime = 0;
        int avgTime = 0;
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while(rowSet.next()) {
                cookTime += rowSet.getInt("cook_time");
                count++;
            }
            if (count != 0) {
                avgTime = cookTime / count;
            }
            jdbcTemplate.update(sql2, avgTime, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Recipe mapRowToRecipe(SqlRowSet rs, boolean isCategory) {
        Recipe recipe = new Recipe();

        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setUserId(rs.getInt("user_id"));
        recipe.setRecipeName(rs.getString("recipe_name"));
        recipe.setAvgCookTime(rs.getInt("avg_cook_time"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImageId(rs.getInt("image_id"));
        recipe.setPublic(rs.getBoolean("is_public"));
        recipe.setCategoryId(rs.getInt("category_id"));
        if (isCategory) {
            recipe.setCategoryName(rs.getString("category_name"));
        }

        return recipe;
    }
}
