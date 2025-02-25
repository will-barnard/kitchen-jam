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
import java.util.UUID;

@Component
public class JdbcRecipeDao implements RecipeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Recipe getRecipe(int recipeId) {

        Recipe recipe = null;
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "LEFT JOIN category on recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "JOIN meal on recipe.recipe_id = meal.recipe_id " +
                "LEFT JOIN category on recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
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
                "VALUES (?, ?, ?, ?, (SELECT default_public FROM user_attributes WHERE user_id = ?)) " +
                "RETURNING recipe_id;";

        try {
            int recipeId = jdbcTemplate.queryForObject(sql, int.class, recipe.getUserId(),
                    recipe.getRecipeName(), recipe.getDescription(), recipe.getCategoryId(), recipe.getUserId());
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
    public Recipe getPublicRecipe(String uuid) {
        Recipe recipe = null;
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.public_url = ? " +
                "AND recipe.is_public = true;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uuid);
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
    public String makePublic(Recipe recipe) {


        // todo refactor this into one sql query

        String uuid = UUID.randomUUID().toString();

        String sql = "SELECT public_url " +
                "FROM recipe " +
                "WHERE recipe_id = ?;";
        String sql2 = "UPDATE recipe " +
                "SET is_public = true, public_url = ? " +
                "WHERE recipe_id = ?;";
        String sql3 = "UPDATE recipe " +
                "SET is_public = true " +
                "WHERE recipe_id = ?;";

        try {
            String result = "";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId());
            if (rs.next()) {
                result = rs.getString("public_url");
            }
            if (result == null) {
                int rowsAffected = 0;
                rowsAffected = jdbcTemplate.update(sql2, uuid, recipe.getRecipeId());
                if (rowsAffected == 0) {
                    throw new DaoException("Something went wrong, no rows affected");
                }
            } else {
                int rowsAffected = 0;
                rowsAffected = jdbcTemplate.update(sql3, recipe.getRecipeId());
                if (rowsAffected == 0) {
                    throw new DaoException("Something went wrong, no rows affected");
                }
                uuid = result;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return uuid;
    }

    @Override
    public void makePrivate(Recipe recipe) {
        String sql = "UPDATE recipe " +
                "SET is_public = false, public_url = ? " +
                "WHERE recipe_id = ?;";
        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, null, recipe.getRecipeId());
            if (rowsAffected == 0) {
                throw new DaoException("Something went wrong, no rows affected");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void deleteRecipeById(int recipeId) {

        String sql = "UPDATE meal SET recipe_id = NULL " +
                "WHERE recipe_id = ?; " +
                "DELETE FROM step " +
                "WHERE recipe_id = ?; " +
                "DELETE FROM ingredient " +
                "WHERE recipe_id  = ?; " +
                "DELETE FROM recipe " +
                "WHERE recipe_id = ?;";

        try {

            jdbcTemplate.update(sql, recipeId, recipeId, recipeId, recipeId);

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
        recipe.setUserName(rs.getString("display_name"));
        recipe.setRecipeName(rs.getString("recipe_name"));
        recipe.setAvgCookTime(rs.getInt("avg_cook_time"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImageId(rs.getInt("image_id"));
        recipe.setPublic(rs.getBoolean("is_public"));
        recipe.setCategoryId(rs.getInt("category_id"));
        recipe.setPublicUrl(rs.getString("public_url"));
        if (isCategory) {
            recipe.setCategoryName(rs.getString("category_name"));
        }

        return recipe;
    }
}
