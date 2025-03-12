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
import org.springframework.security.core.parameters.P;
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
    private IngredientDao ingredientDao;


    @GetMapping(path = "/{mealId}")
    public Meal getMealById(@PathVariable int mealId, Principal principal) {
        Meal meal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (mealDao.verifyMealOwner(userId, mealId)) {
                meal = mealDao.getMeal(mealId);
                meal.setTags(tagsDao.getTagsByMealId(mealId));
                meal.setIngredientList(ingredientDao.getIngredientsByMeal(mealId));
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meal;
    }

    @PreAuthorize("permitAll")
    @GetMapping(path = "/public/{uuid}")
    public Meal getPublicMeal(@PathVariable String uuid) {
        Meal meal = null;
        try {
            meal = mealDao.getPublicMeal(uuid);
            meal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
            meal.setIngredientList(ingredientDao.getIngredientsByMeal(meal.getMealId()));
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
            meals = ingredientDao.getIngredientsByMeals(meals);
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
        if (result.size() > 0) {
            result = ingredientDao.getIngredientsByMeals(result);
        }
        return result;
    }

    @GetMapping(path = "/tag")
    public List<Meal> getMealsByTag(@RequestBody Tag tag, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> meals = null;
        try {
            meals = mealDao.getMealsByTagAndUser(tag.getTagId(), userId);
            meals = ingredientDao.getIngredientsByMeals(meals);
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
            meals = ingredientDao.getIngredientsByMeals(meals);
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
        meal.setPublic(false);
        Meal newMeal = null;
        try {
            newMeal = mealDao.createMeal(meal);
            if (meal.getIngredientList() == null) {
                // do nothing
            } else if (!meal.getIngredientList().isEmpty()) {
                newMeal = ingredientDao.addIngredientsToMeal(meal.getIngredientList(), newMeal);
            }

        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newMeal;
    }

    @PutMapping(path = "")
    public Meal updateMeal(@RequestBody Meal meal, Principal principal) {

        if (meal.getRecipeId() == null) {
        } else if (meal.getRecipeId() == 0) {
            meal.setRecipeId(null);
        }

        meal.setLastModified(LocalDateTime.now());
        Meal updatedMeal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            Meal getMeal = mealDao.getMeal(meal.getMealId());
            if (getMeal.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            Integer oldRecipeId = getMeal.getRecipeId();
            updatedMeal = mealDao.updateMeal(meal);
            ingredientDao.deleteAllIngredientsFromMeal(meal.getMealId());
            if (meal.getIngredientList() == null) {
                // do nothing
            } else if (!meal.getIngredientList().isEmpty()) {
                updatedMeal = ingredientDao.addIngredientsToMeal(meal.getIngredientList(), meal);
            }

            updatedMeal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return updatedMeal;
    }

    @PostMapping("/public/{mealId}")
    public String makePublic(@PathVariable int mealId, Principal principal) {
        Meal meal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            meal = mealDao.getMeal(mealId);
            if (meal.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            meal.setPublic(true);
            meal.setPublicUrl(mealDao.makePublic(meal));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        String url = "http://kitchen-jam.com/p/meal/" + meal.getPublicUrl();
        return url;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/public/{mealId}")
    public void makePrivate(@PathVariable int mealId, Principal principal) {
        Meal meal = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            meal = mealDao.getMeal(mealId);
            if (meal.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            meal.setPublic(false);
            mealDao.makePrivate(meal);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
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
            ingredientDao.deleteAllIngredientsFromMeal(mealId);
            mealDao.deleteMealById(mealId);

        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }
}
