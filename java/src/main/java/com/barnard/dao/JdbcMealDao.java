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

@Component
public class JdbcMealDao implements MealDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Meal getMeal(int mealId) {

        Meal meal = null;
        String sql = "SELECT * " +
                "from meal " +
                "where meal_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, mealId);
            if (rowSet.next()) {
                meal = mapRowToMeal(rowSet);
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
        String sql = "SELECT * " +
                "from meal " +
                "where meal_name LIKE ? " +
                "AND user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
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
        String sql = "SELECT * " +
                "from meal " +
                "join tags_meal on meal.meal_id = tags_meal.meal_id " +
                "join tags on tags_meal.tag_id = tags.tag_id " +
                "where tags.tag_id = ? " +
                "AND meal.user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagId, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
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
        String sql = "SELECT * " +
                "FROM meal " +
                "WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
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
                "WHERE recipe_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
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

        String sql = "INSERT into meal (user_id, recipe_id, meal_name, meal_comment, date_created, cook_time, notes, ingredients, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING meal_id;";

        try {

            int mealId = jdbcTemplate.queryForObject(sql, int.class, meal.getUserId(), meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDate(), meal.getCookTime(),
                    meal.getNotes(), meal.getIngredients(), meal.getRating());
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
                "SET user_id = ?, recipe_id = ?, meal_name = ?, mean_comment = ?, " +
                "date_created = ?, cook_time = ?, notes = ?, ingredients = ?, " +
                "rating = ? " +
                "WHERE meal_id = ?;";

        try {
            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, meal.getUserId(), meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDate(), meal.getCookTime(),
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
    public void deleteMealById(int mealId) {

        String sql = "DELETE FROM tags_meal " +
                "WHERE meal_id  = ?;";
        String sql2 = "DELETE FROM meal_image " +
                "WHERE meal_id = ?;";
        String sql3 = "DELETE FROM meal " +
                "WHERE meal_id = ?;";

        try {

            jdbcTemplate.update(sql, mealId);
            jdbcTemplate.update(sql2, mealId);
            int rowsAffected = jdbcTemplate.update(sql3, mealId);
            if (rowsAffected != 1) {
                throw new DaoException();
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Meal mapRowToMeal(SqlRowSet rs) {
        Meal meal = new Meal();

        meal.setMealId(rs.getInt("meal_id"));
        meal.setMealName(rs.getString("meal_name"));
        meal.setRecipeId(rs.getInt("recipe_id"));
        meal.setMealComment(rs.getString("meal_comment"));
        meal.setDate(rs.getDate("date_created").toLocalDate());
        meal.setCookTime(rs.getInt("cook_time"));
        meal.setNotes(rs.getString("notes"));
        meal.setIngredients(rs.getString("ingredients"));
        meal.setRating(rs.getInt("rating"));
        meal.setImageId(rs.getInt("image_id"));

        return meal;
    }

}
