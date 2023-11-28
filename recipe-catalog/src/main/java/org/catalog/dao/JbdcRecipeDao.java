package org.catalog.dao;

import javax.sql.DataSource;


public class JbdcRecipeDao implements RecipeDao {

    private final String RECIPE_SELECT = "SELECT r.recipe_id, r.recipe_name, r.avg_cook_time, r.notes FROM recipe r ";






}
