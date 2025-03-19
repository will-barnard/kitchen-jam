package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProfileDao implements ProfileDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UserProfilePrimitive> searchUsers(String search, int userId) {

        search = "%" + search.toLowerCase() + "%";
        List<UserProfilePrimitive> list = new ArrayList<>();
        String sql = "SELECT * FROM user_attributes " +
                "WHERE LOWER (display_name) LIKE ? " +
                "AND is_public = true " +
                "AND user_id != ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);
            while (rowSet.next()) {
                list.add(mapRowToProfilePrimitive(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return list;
    }

    @Override
    public UserProfile getUserProfile(int userId) {

        UserProfile profile = null;
        String sql = "SELECT " +
                "ua.*, " +
                "COALESCE(m.meal_count, 0) AS meal_count, " +
                "COALESCE(r.recipe_count, 0) AS recipe_count " +
                "FROM user_attributes ua " +
                "LEFT JOIN (" +
                    "SELECT user_id, COUNT(meal_id) AS meal_count " +
                    "FROM meal " +
                    "GROUP BY user_id " +
                ") m ON ua.user_id = m.user_id " +
                "LEFT JOIN (" +
                    "SELECT user_id, COUNT(recipe_id) AS recipe_count " +
                    "FROM recipe " +
                    "GROUP BY user_id " +
                ") r ON ua.user_id = r.user_id " +
                "WHERE ua.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()) {
                profile = mapRowToProfile(rowSet);
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return profile;
    }

    @Override
    public UserProfile updateUserProfile(UserProfile userProfile) {

        if (userProfile.getImageId() == 0) {
            userProfile.setImageId(null);
        }

        UserProfile updatedProfile;
        String sql = "UPDATE user_attributes " +
                "SET display_name = ?, " +
                "image_id = ?, headline = ?, bio = ?, user_location = ?, " +
                "favorite_foods = ?, favorite_cuisines = ? " +
                "WHERE user_id = ?;";

        try {
            jdbcTemplate.update(sql, userProfile.getDisplayName(),
                    userProfile.getImageId(), userProfile.getHeadline(), userProfile.getBio(), userProfile.getLocation(),
                    userProfile.getFavoriteFoods(), userProfile.getFavoriteCuisines(), userProfile.getUserId());
            updatedProfile = getUserProfile(userProfile.getUserId());
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedProfile;
    }

    // todo move these to appropriate daos not here
    @Override
    public List<Meal> getUserFeedMeals(int userId) {

        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT meal.*, recipe.recipe_name, user_attributes.display_name " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "JOIN user_attributes ON meal.user_id = user_attributes.user_id " +
                "WHERE meal.user_id = ? " +
                "AND is_public = true " +
                "ORDER BY date_created DESC;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
            }
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return meals;
    }

    @Override
    public List<Recipe> getUserFeedRecipes(int userId) {

        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT recipe.*, category.category_name, user_attributes.display_name, " +
                "(SELECT AVG(rating) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_rating, " +
                "(SELECT AVG(cook_time) FROM meal WHERE meal.recipe_id = recipe.recipe_id) AS avg_cook_time, " +
                "(SELECT MAX(date_cooked) FROM meal WHERE meal.recipe_id = recipe.recipe_id) as lastCreated " +
                "FROM recipe " +
                "LEFT JOIN category ON recipe.category_id = category.category_id " +
                "JOIN user_attributes ON recipe.user_id = user_attributes.user_id " +
                "WHERE recipe.user_id = ? " +
                "ANDS is_public = true;";

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

    private UserProfilePrimitive mapRowToProfilePrimitive(SqlRowSet rs) {
        UserProfilePrimitive profile = new UserProfilePrimitive();

        profile.setUserId(rs.getInt("user_id"));
        profile.setDisplayName(rs.getString("display_name"));

        return profile;
    }

    private UserProfile mapRowToProfile(SqlRowSet rs) {
        UserProfile profile = new UserProfile();

        profile.setUserId(rs.getInt("user_id"));
        profile.setDisplayName(rs.getString("display_name"));
        profile.setPublic(rs.getBoolean("is_public"));
        profile.setDefaultPublic(rs.getBoolean("default_public"));
        profile.setImageId(rs.getInt("image_id"));
        profile.setHeadline(rs.getString("headline"));
        profile.setBio(rs.getString("bio"));
        profile.setLocation(rs.getString("user_location"));
        profile.setFavoriteFoods(rs.getString("favorite_foods"));
        profile.setFavoriteCuisines(rs.getString("favorite_cuisines"));
        if (rs.getTimestamp("created_at") != null) {
            profile.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        profile.setCountMeals(rs.getInt("meal_count"));
        profile.setCountRecipes(rs.getInt("recipe_count"));

        return profile;
    }

    private Meal mapRowToMeal(SqlRowSet rs) {
        Meal meal = new Meal();

        meal.setMealId(rs.getInt("meal_id"));
        meal.setUserId(rs.getInt("user_id"));
        meal.setUserName(rs.getString("display_name"));
        meal.setMealName(rs.getString("meal_name"));
        if (rs.getObject("recipe_id") != null) {
            meal.setRecipeId(rs.getInt("recipe_id"));
            meal.setRecipeName(rs.getString("recipe_name"));
        }
        meal.setMealComment(rs.getString("meal_comment"));
        if (rs.getDate("date_cooked") != null) {
            meal.setDateCooked(rs.getDate("date_cooked").toLocalDate());
        }
        if (rs.getTimestamp("date_created") != null) {
            meal.setDateCreated(rs.getTimestamp("date_created").toLocalDateTime());
        }
        if (rs.getTimestamp("last_modified") != null) {
            meal.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
        }
        meal.setCookTime(rs.getInt("cook_time"));
        meal.setNotes(rs.getString("notes"));
        meal.setIngredients(rs.getString("ingredients"));
        meal.setRating(rs.getInt("rating"));
        meal.setImageId(rs.getInt("image_id"));
        meal.setPublic(rs.getBoolean("is_public"));
        meal.setPublicUrl(rs.getString("public_url"));

        return meal;
    }

    private Recipe mapRowToRecipe(SqlRowSet rs) {
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
        recipe.setCategoryName(rs.getString("category_name"));
        if (rs.getDate("last_created") != null) {
            recipe.setLastCreated(rs.getDate("last_created").toLocalDate());
        }

        return recipe;
    }
}
