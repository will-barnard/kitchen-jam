package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Meal;
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
public class JdbcMealDao implements MealDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Meal getMeal(int mealId) {

        Meal meal = null;
        String sql = "SELECT meal.*, recipe.recipe_name " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE meal_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);
            if (rowSet.next()) {
                meal = mapRowToMeal(rowSet, true);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meal;
    }

    @Override
    public List<Meal> searchLikeMeals(String search, int userId) {

        List<Meal> meals = new ArrayList<Meal>();
        search = "%" + search + "%";
        String sql = "SELECT meal.*, recipe.recipe_name " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE meal_name LIKE ? " +
                "AND user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet, true));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meals;
    }

    @Override
    public List<Meal> getMealsByTagAndUser(int tagId, int userId) {

        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal.*, recipe.recipe_name " +
                "FROM meal " +
                "JOIN tags_meal ON meal.meal_id = tags_meal.meal_id " +
                "JOIN tags ON tags_meal.tag_id = tags.tag_id " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE tags.tag_id = ? " +
                "AND meal.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagId, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet, true));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meals;
    }

    @Override
    public List<Meal> getMealsByUserId(int userId) {

        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT meal.*, recipe.recipe_name " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE meal.user_id = ? " +
                "ORDER BY date_created DESC;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet, true));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meals;
    }

    @Override
    public List<Meal> getMealsByRecipeId(int recipeId) {

        List<Meal> meals = new ArrayList<Meal>();
        String sql = "SELECT * " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE meal.recipe_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet, false));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meals;
    }

    @Override
    public Meal createMeal(Meal meal) {

        String sql = "INSERT into meal (user_id, recipe_id, meal_name, meal_comment, date_cooked, " +
                "cook_time, notes, ingredients, rating, date_created, last_modified) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING meal_id;";

        try {

            int mealId = jdbcTemplate.queryForObject(sql, int.class, meal.getUserId(), meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDateCooked(), meal.getCookTime(),
                    meal.getNotes(), meal.getIngredients(), meal.getRating(), meal.getDateCreated(), meal.getLastModified());
            meal.setMealId(mealId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meal;
    }

    @Override
    public Meal updateMeal(Meal meal) {

        String sql = "UPDATE meal " +
                "SET recipe_id = ?, meal_name = ?, meal_comment = ?, " +
                "date_cooked = ?, last_modified = ?, cook_time = ?, notes = ?, ingredients = ?, " +
                "rating = ? " +
                "WHERE meal_id = ?;";

        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDateCooked(), meal.getLastModified(), meal.getCookTime(),
                    meal.getNotes(), meal.getIngredients(), meal.getRating(), meal.getMealId());
            if (rowsAffected == 0) {
                throw new DaoException("Something went wrong, no rows affected");
            }
            meal = getMeal(meal.getMealId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return meal;
    }

    @Override
    public Meal getPublicMeal(String uuid) {
        Meal meal = null;
        String sql = "SELECT meal.*, recipe.recipe_name " +
                "FROM meal " +
                "LEFT JOIN recipe ON meal.recipe_id = recipe.recipe_id " +
                "WHERE meal.public_url = ? " +
                "AND is_public = true;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, uuid);
            if (rowSet.next()) {
                meal = mapRowToMeal(rowSet, true);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return meal;
    }

    @Override
    public String makePublic(Meal meal) {

        // todo refactor this into one sql query
        String uuid = UUID.randomUUID().toString();

        String sql = "SELECT public_url " +
                "FROM meal " +
                "WHERE meal_id = ?;";
        String sql2 = "UPDATE meal " +
                "SET is_public = true, public_url = ? " +
                "WHERE meal_id = ?;";
        String sql3 = "UPDATE meal " +
                "SET is_public = true " +
                "WHERE meal_id = ?;";

        try {
            String result = "";
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, meal.getMealId());
            if (rs.next()) {
                result = rs.getString("public_url");
            }
            if (result == null) {
                int rowsAffected = 0;
                rowsAffected = jdbcTemplate.update(sql2, uuid, meal.getMealId());
                if (rowsAffected == 0) {
                    throw new DaoException("Something went wrong, no rows affected");
                }
            } else {
                int rowsAffected = 0;
                rowsAffected = jdbcTemplate.update(sql3, meal.getMealId());
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
    public void makePrivate(Meal meal) {
        String sql = "UPDATE meal " +
                "SET is_public = false " +
                "WHERE meal_id = ?;";
        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, meal.getMealId());
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
    public void deleteMealById(int mealId) {

        String sql = "DELETE FROM tags_meal " +
                "WHERE meal_id  = ?;";
        String sql2 = "DELETE FROM meal " +
                "WHERE meal_id = ?;";

        try {

            jdbcTemplate.update(sql, mealId);
            int rowsAffected = jdbcTemplate.update(sql2, mealId);
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
    public boolean verifyMealOwner(int userId, int mealId) {
        String sql = "SELECT user_id FROM meal " +
                "WHERE meal_id = ?;";

        try {

            int result = jdbcTemplate.queryForObject(sql, int.class, mealId);
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

    private Meal mapRowToMeal(SqlRowSet rs, boolean isRecipeName) {
        Meal meal = new Meal();

        meal.setMealId(rs.getInt("meal_id"));
        meal.setUserId(rs.getInt("user_id"));
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


}
