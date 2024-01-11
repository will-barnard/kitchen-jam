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
        return null;
    }

    @Override
    public List<Meal> getMealsByUserId(int userId) {
        return null;
    }

    @Override
    public List<Meal> getMealsByRecipeId(int recipeId) {
        return null;
    }

    @Override
    public Meal createMeal(Meal meal) {
        return null;
    }

    @Override
    public Meal updateMeal(Meal meal) {
        return null;
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
