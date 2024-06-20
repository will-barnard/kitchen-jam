package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.exception.AuthException;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import com.barnard.model.Tag;
import com.barnard.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/meal")
public class MealController {

    // todo add authentication and security when doing GET
    // todo add tags when getting single meal

    @Autowired
    private MealDao mealDao;
    @Autowired
    private TagsDao tagsDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private EmailService emailService;

    @GetMapping(path="/test")
    public void test() {
//        emailService.sendEmail("barnardwill@gmail.com", "test", "test");
    }

    @GetMapping(path = "/{mealId}")
    public Meal getMealById(@PathVariable int mealId, Principal principal) {
        Meal meal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (mealDao.verifyMealOwner(userId, mealId)) {
                meal = mealDao.getMeal(mealId);
                meal.setTags(tagsDao.getTagsByMealId(mealId));
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
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
        meal.setDateCreated(LocalDateTime.now());
        meal.setLastModified(LocalDateTime.now());
        Meal newMeal = null;
        try {
            newMeal = mealDao.createMeal(meal);
            if (meal.getRecipeId() != null || meal.getRecipeId() != 0) {
                recipeDao.aggregateRecipeData(meal.getRecipeId());
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newMeal;
    }

    @PutMapping(path = "")
    public Meal updateMeal(@RequestBody Meal meal, Principal principal) {

        if (meal.getRecipeId() == 0) {
            meal.setRecipeId(null);
        }

        meal.setLastModified(LocalDateTime.now());
        Meal updatedMeal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (mealDao.getMeal(meal.getMealId()).getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            Integer oldRecipeId = mealDao.getMeal(meal.getMealId()).getRecipeId();
            mealDao.updateMeal(meal);
            updatedMeal = mealDao.getMeal(meal.getMealId());
            if (meal.getRecipeId() != null) {
                recipeDao.aggregateRecipeData(meal.getRecipeId());
            }
            if (!updatedMeal.getRecipeId().equals(oldRecipeId)) {
                recipeDao.aggregateRecipeData(oldRecipeId);
            }
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
            Integer recipeId = mealDao.getMeal(mealId).getRecipeId();
            mealDao.deleteMealById(mealId);
            if (recipeId != 0) {
                recipeDao.aggregateRecipeData(recipeId);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }
}
