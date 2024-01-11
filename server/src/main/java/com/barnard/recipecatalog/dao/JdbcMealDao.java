package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;

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
        } catch (Exception e) {
            System.out.println("Something went wrong getting a meal");
        }

        return meal;

    }

    @Override
    public List<Meal> searchLikeMeals(String search) {

        List<Meal> meals = new ArrayList<Meal>();
        search = "%" + search + "%";

        String sql = "SELECT * " +
                "from meal " +
                "where meal_name LIKE ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong getting a meals like search");
        }

        return meals;
    }

    @Override
    public List<Meal> getMealsByTag(int tagId) {

        List<Meal> meals = new ArrayList<Meal>();

        String sql = "SELECT * " +
                "from meal " +
                "join tags_meal on meal.meal_id = tags_meal.meal_id " +
                "join tags on tags_meal.tag_id = tags.tag_id " +
                "where tags.tag_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, tagId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong getting meals by tag");
        }

        return meals;

    }

    @Override
    public List<Meal> getMealsByUserId(int userId) {

        List<Meal> meals = new ArrayList<Meal>();

        String sql = "SELECT * " +
                "from meal " +
                "where user_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong getting meals by user");
        }

        return meals;

    }

    @Override
    public List<Meal> getMealsByRecipeId(int recipeId) {

        List<Meal> meals = new ArrayList<Meal>();

        String sql = "SELECT * " +
                "from meal " +
                "where recipe_id = ?;";

        try {

            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rowSet.next()) {
                meals.add(mapRowToMeal(rowSet));
            }
        } catch (Exception e) {
            System.out.println("Something went wrong getting meals by recipe");
        }

        return meals;

    }

    @Override
    public int createMeal(Meal meal) {

        String sql = "INSERT into meal (user_id, recipe_id, meal_name, meal_comment, date_created, cook_time, notes, ingredients, rating) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "RETURNING meal_id;";
        int mealId = 0;

        try {

            mealId = jdbcTemplate.queryForObject(sql, int.class, meal.getUserId(), meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDate(), meal.getCookTime(),
                    meal.getNotes(), meal.getIngredients(), meal.getRating());

        } catch (Exception e) {
            System.out.println("Something went wrong creating a meal");
        }

        return mealId;

    }

    @Override
    public Meal updateMeal(Meal meal) {

        String sql = "UPDATE meal " +
                "SET user_id = ?, recipe_id = ?, meal_name = ?, mean_comment = ?, " +
                "date_created = ?, cook_time = ?, notes = ?, ingredients = ?, " +
                "rating = ?, image_id = ? " +
                "WHERE meal_id = ?;";

        try {

            int rowsAffected = 0;
            rowsAffected = jdbcTemplate.update(sql, meal.getUserId(), meal.getRecipeId(),
                    meal.getMealName(), meal.getMealComment(), meal.getDate(), meal.getCookTime(),
                    meal.getNotes(), meal.getIngredients(), meal.getRating(), meal.getMealId());

            if (rowsAffected == 0) {
                throw new Exception("Something went wrong, no rows affected");
            }
            else if (rowsAffected > 1) {
                throw new Exception("Something went wrong, more than one row affected");
            }
            else {
                meal = getMeal(meal.getMealId());
            }

        } catch (Exception e) {
            System.out.println("Something went wrong updating a meal");
        }

        return meal;

    }

    @Override
    public int deleteMealById(int mealId) {
        return 0;
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
