package com.barnard.dao;


import com.barnard.exception.DaoException;
import com.barnard.model.Friend;
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE recipe_id = ?) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE recipe_id = ?) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE recipe_id = ?) as last_created " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.recipe_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId, recipeId, recipeId, recipeId);

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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
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
    public List<Recipe> getUserProfileRecipes(int userId) {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.user_id = ? " +
                "AND recipe.is_public = true;";

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
    public List<Recipe> getRecipeFeed(int userId, List<Friend> friendsList) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.is_public = true " +
                "AND recipe.user_id = ";
        for (Friend friend : friendsList) {
            sql += friend.getFriendId() + " OR meal.user_id = ";
        }
        sql = sql.substring(0, sql.length() - 18);
        sql +=  " AND recipe.is_public = true " +
                "ORDER BY last_created DESC " +
                "LIMIT 10;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
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
    public List<Recipe> getMoreRecipes(int userId, List<Friend> friendsList, int timesLoaded) {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as last_created " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.is_public = true " +
                "AND recipe.user_id = ";
        for (Friend friend : friendsList) {
            sql += friend.getFriendId() + " OR meal.user_id = ";
        }
        sql = sql.substring(0, sql.length() - 18);
        sql +=  " AND recipe.is_public = true " +
                "ORDER BY last_created DESC " +
                "LIMIT 10 " +
                "OFFSET ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, (timesLoaded * 10));
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
    public Recipe createRecipe(Recipe recipe) {
        Recipe newRecipe = new Recipe();
        String uuid = UUID.randomUUID().toString();
        String sql = "INSERT INTO recipe (user_id, recipe_name, description, category_id, is_public, public_url) " +
                "VALUES (?, ?, ?, ?, (SELECT default_public FROM user_attributes WHERE user_id = ?), ?) " +
                "RETURNING recipe_id;";

        try {
            int recipeId = jdbcTemplate.queryForObject(sql, int.class, recipe.getUserId(),
                    recipe.getRecipeName(), recipe.getDescription(), recipe.getCategoryId(), recipe.getUserId(), uuid);
            recipe.setRecipeId(recipeId);
            newRecipe = getRecipe(recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newRecipe;

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
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal " +
                "JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE recipe.public_url = ?) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal " +
                "JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE recipe.public_url = ?) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal " +
                "JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE recipe.public_url = ?) as last_created " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.public_url = ? " +
                "AND recipe.is_public = true;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uuid, uuid, uuid, uuid);
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
        String result = "";
        String sql = "UPDATE recipe " +
                "SET is_public = true " +
                "WHERE recipe_id = ? " +
                "RETURNING public_url;";

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, recipe.getRecipeId());
            if (rs.next()) {
                result = rs.getString("public_url");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    @Override
    public void makePrivate(Recipe recipe) {
        String sql = "UPDATE recipe " +
                "SET is_public = false " +
                "WHERE recipe_id = ?;";
        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, recipe.getRecipeId());
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


    private Recipe mapRowToRecipe(SqlRowSet rs, boolean isCategory) {
        Recipe recipe = new Recipe();

        recipe.setRecipeId(rs.getInt("recipe_id"));
        recipe.setUserId(rs.getInt("user_id"));
        recipe.setUserName(rs.getString("display_name"));
        recipe.setRecipeName(rs.getString("recipe_name"));
        recipe.setAvgCookTime(rs.getBigDecimal("avg_cook_time"));
        recipe.setAvgRating(rs.getBigDecimal("avg_rating"));
        recipe.setDescription(rs.getString("description"));
        recipe.setImageId(rs.getInt("image_id"));
        recipe.setPublic(rs.getBoolean("is_public"));
        recipe.setCategoryId(rs.getInt("category_id"));
        recipe.setPublicUrl(rs.getString("public_url"));
        if (isCategory) {
            recipe.setCategoryName(rs.getString("category_name"));
        }
        if (rs.getDate("last_created") != null) {
            recipe.setLastCreated(rs.getDate("last_created").toLocalDate());
        }
        return recipe;
    }
}
