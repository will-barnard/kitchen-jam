package com.barnard.controller;

import com.barnard.dao.ImageDao;
import com.barnard.dao.MealDao;
import com.barnard.dao.TagsDao;
import com.barnard.dao.UserDao;
import com.barnard.exception.AuthException;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import com.barnard.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/meal")
public class MealController {

    // todo add authentication and security when doing GET

    @Autowired
    private MealDao mealDao;
    @Autowired
    private TagsDao tagsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;

    @GetMapping(path = "/{mealId}")
    public Meal getMealById(@PathVariable int mealId) {
        Meal meal = mealDao.getMeal(mealId);
        return meal;
    }

    @GetMapping(path = "/search")
    public List<Meal> searchMeals(@RequestBody String search, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> meals = null;
        try {
            meals = mealDao.searchLikeMeals(search, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meals;
    }

    @GetMapping(path = "/user")
    public List<Meal> getMealsByUser(Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> result = mealDao.getMealsByUserId(userId);
        for (Meal meal : result) {
            meal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
        }
        return result;
    }

    @GetMapping(path = "/tag")
    public List<Meal> getMealsByTag(@RequestBody Tag tag, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> meals = null;
        try {
            meals = mealDao.getMealsByTagAndUser(tag.getTagId(), userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meals;
    }

    @GetMapping(path = "/recipe")
    public List<Meal> getMealsByRecipe(@RequestBody Recipe recipe) {
        List<Meal> meals = null;
        try {
            meals = mealDao.getMealsByRecipeId(recipe.getRecipeId());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meals;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Meal createMeal(@RequestBody Meal meal, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        meal.setUserId(userId);
        Meal newMeal = null;
        try {
            newMeal = mealDao.createMeal(meal);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newMeal;
    }

    @PutMapping(path = "/update")
    public Meal updateMeal(@RequestBody Meal meal, Principal principal) {
        if (meal.getRecipeId() == 0) {
            meal.setRecipeId(null);
        }
        Meal updatedMeal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (mealDao.getMeal(meal.getMealId()).getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            mealDao.updateMeal(meal);
            updatedMeal = mealDao.getMeal(meal.getMealId());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return updatedMeal;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{mealId}")
    public void deleteMeal(@PathVariable int mealId, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (mealDao.getMeal(mealId).getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            mealDao.deleteMealById(mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }
}
