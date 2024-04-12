package com.barnard.controller;

import com.barnard.dao.CategoryDao;
import com.barnard.dao.MealDao;
import com.barnard.dao.RecipeDao;
import com.barnard.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryController {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MealDao mealDao;



}
