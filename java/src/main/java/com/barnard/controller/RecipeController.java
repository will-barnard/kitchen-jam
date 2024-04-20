package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.exception.AuthException;
import com.barnard.model.Recipe;
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
    private UserDao userDao;
    @Autowired
    private ImageDao imageDao;

    @GetMapping(path = "/{recipeId}")
    public Recipe getRecipe(@PathVariable int recipeId) {
        Recipe recipe = null;
        try {
            recipe = recipeDao.getRecipe(recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @GetMapping(path = "/search")
    public List<Recipe> searchRecipes(@RequestBody String search, Principal principal) {
        List<Recipe> recipes = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            recipes = recipeDao.searchLikeRecipes(search, userId);
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

        // todo add public option on frontend
        recipe.setPublic(true);

        int userId = userDao.getUserByUsername(principal.getName()).getId();
        recipe.setUserId(userId);
        try {
            recipeDao.createRecipe(recipe);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
    }

    @PutMapping(path = "")
    public Recipe updateRecipe(@RequestBody Recipe recipe, Principal principal) {

        // todo add public option on frontend
        recipe.setPublic(true);

        int userId = userDao.getUserByUsername(principal.getName()).getId();
        recipe.setUserId(userId);
        try {
            int userAuth = recipeDao.getRecipe(recipe.getRecipeId()).getUserId();
            if (userAuth != userId) {
                throw new AuthException("Not authorized");
            }
            recipe = recipeDao.updateRecipe(recipe);
        } catch(AuthException e) {
            throw new AuthException(e.getMessage());
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipe;
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
