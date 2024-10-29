package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.exception.AuthException;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import com.barnard.model.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.security.Principal;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/recipe")
public class RecipeController {

    // todo add authentication and security when doing GET

    @Autowired
    private MealDao mealDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private TagsDao tagsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;
    @Autowired
    private StepDao stepDao;

    @GetMapping(path = "/{recipeId}")
    public Recipe getRecipe(@PathVariable int recipeId) {
        Recipe recipe = null;
        try {
            recipe = recipeDao.getRecipe(recipeId);
            recipe.setMealList(mealDao.getMealsByRecipeId(recipeId));
            for (Meal meal : recipe.getMealList()) {
                meal.setRecipeName(recipe.getRecipeName());
                meal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @GetMapping(path = "/public/{uuid}")
    public Recipe getPublicRecipe(@PathVariable String uuid) {
        Recipe recipe = null;
        try {
            recipe = recipeDao.getPublicRecipe(uuid);
            recipe.setMealList(mealDao.getMealsByRecipeId(recipe.getRecipeId()));
            recipe.setStepList(stepDao.getStepsByRecipe(recipe.getRecipeId()));
            for (Meal meal : recipe.getMealList()) {
                meal.setRecipeName(recipe.getRecipeName());
                meal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @PostMapping(path = "/search")
    public List<Recipe> searchRecipes(@RequestBody Recipe recipe, Principal principal) {
        List<Recipe> recipes = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipes = recipeDao.searchLikeRecipes(recipe.getRecipeName(), userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipes;
    }

    @GetMapping(path = "/user")
    public List<Recipe> getRecipesByUser(Principal principal) {
        List<Recipe> recipes = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipes = recipeDao.getRecipesByUserId(userId);
            recipes = stepDao.getSteplist(recipes);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipes;
    }

    @GetMapping(path = "/category/{categoryId}")
    public List<Recipe> getRecipesByCategory(@PathVariable int categoryId, Principal principal) {
        List<Recipe> recipes = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipes = recipeDao.getRecipesByCategoryId(categoryId, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipes;
    }

    @GetMapping(path = "/meal/{mealId}")
    public Recipe getRecipeByMeal(@PathVariable int mealId, Principal principal) {
        Recipe recipe = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipe = recipeDao.getRecipeByMealId(mealId);
            if(recipe.getUserId() != userId) {
                recipe = null;
                throw new AuthException("User not authorized");
            }
        } catch(AuthException e) {
            throw new AuthException(e.getMessage());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Recipe createRecipe(@RequestBody Recipe recipe, Principal principal) {

        recipe.setPublic(false);

        int userId = userDao.getUserByUsername(principal.getName()).getId();
        recipe.setUserId(userId);
        try {
            recipeDao.createRecipe(recipe);
            if (recipe.isUpdateSteps()) {
                for (Step step : recipe.getStepList()) {
                    stepDao.createStep(step);
                }
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @PutMapping(path = "")
    public Recipe updateRecipe(@RequestBody Recipe recipe, Principal principal) {

        if (recipe.getCategoryId() == 0) {
            recipe.setCategoryId(null);
        }

        int userId = userDao.getUserByUsername(principal.getName()).getId();
        recipe.setUserId(userId);
        try {
            int userAuth = recipeDao.getRecipe(recipe.getRecipeId()).getUserId();
            if (userAuth != userId) {
                throw new AuthException("Not authorized");
            }
            if (recipe.isUpdateSteps()) {
                stepDao.deleteAllStepsFromRecipe(recipe.getRecipeId());
                for (Step step : recipe.getStepList()) {
                    step.setUserId(userId);
                    step.setRecipeId(recipe.getRecipeId());
                    stepDao.createStep(step);
                }
            }
            recipe = recipeDao.updateRecipe(recipe);
            recipe.setStepList(stepDao.getStepsByRecipe(recipe.getRecipeId()));
        } catch(AuthException e) {
            throw new AuthException(e.getMessage());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @PostMapping("/public/{recipeId}")
    public String makePublic(@PathVariable int recipeId, Principal principal) {
        Recipe recipe = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipe = recipeDao.getRecipe(recipeId);
            if (recipe.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            recipe.setPublic(true);
            recipe.setPublicUrl(recipeDao.makePublic(recipe));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        String url = "http://kitchen-jam.com/p/recipe/" + recipe.getPublicUrl();
        return url;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/public/{recipeId}")
    public void makePrivate(@PathVariable int recipeId, Principal principal) {
        Recipe recipe = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipe = recipeDao.getRecipe(recipeId);
            if (recipe.getUserId() != userId) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }
            recipe.setPublic(false);
            recipeDao.makePrivate(recipe);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{recipeId}")
    public void deleteRecipe(@PathVariable int recipeId, Principal principal) {

        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            Recipe recipe = recipeDao.getRecipe(recipeId);
            if (recipe.getUserId() != userId) {
                throw new AuthException("Not authorized");
            } else {
                recipeDao.deleteRecipeById(recipeId);
            }
        } catch(AuthException e) {
            throw new AuthException(e.getMessage());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }
}
