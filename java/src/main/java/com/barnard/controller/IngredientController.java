package com.barnard.controller;

import com.barnard.dao.IngredientDao;
import com.barnard.dao.UserDao;
import com.barnard.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/ingredient")
public class IngredientController {

    @Autowired
    private IngredientDao ingredientDao;
    @Autowired
    private UserDao userDao;

    @GetMapping("/meal/{mealId}")
    public List<Ingredient> getIngredientsByMeal(@PathVariable int mealId) {
        List<Ingredient> ingredients = null;
        try {
            ingredients = ingredientDao.getIngredientsByMeal(mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return ingredients;
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Ingredient> getIngredientsByRecipe(@PathVariable int recipeId) {
        List<Ingredient> ingredients = null;
        try {
            ingredients = ingredientDao.getIngredientsByRecipe(recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return ingredients;
    }

    @PostMapping("/search")
    public List<Ingredient> searchLikeIngredient(@RequestBody String search, Principal principal) {
        List<Ingredient> ingredients = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            ingredients = ingredientDao.searchLikeIngredients(search, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return ingredients;
    }

    @PostMapping
    public Ingredient createIngredient(@RequestBody Ingredient ingredient) {
        Ingredient newIngredient = null;
        try {
            newIngredient = ingredientDao.createIngredient(ingredient);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newIngredient;
    }

    @PutMapping
    public Ingredient updateIngredient(@RequestBody Ingredient ingredient) {
        Ingredient newIngredient = null;
        try {
            newIngredient = ingredientDao.updateIngredient(ingredient);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newIngredient;
    }

    @PutMapping("/meal")
    public List<Ingredient> updateMealIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> newIngredients = null;
        int mealId = ingredients.get(0).getMealId();
        try {
            ingredientDao.deleteAllIngredientsFromMeal(mealId);
            for (Ingredient ingredient : ingredients) {
                ingredientDao.addIngredientToMeal(ingredient);
            }
            newIngredients = ingredientDao.getIngredientsByMeal(mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newIngredients;
    }

    @PutMapping("/recipe")
    public List<Ingredient> updateRecipeIngredients(@RequestBody List<Ingredient> ingredients) {
        List<Ingredient> newIngredients = null;
        int recipeId = ingredients.get(0).getRecipeId();
        try {
            ingredientDao.deleteAllIngredientsFromRecipe(recipeId);
            for (Ingredient ingredient : ingredients) {
                ingredientDao.addIngredientToRecipe(ingredient);
            }
            newIngredients = ingredientDao.getIngredientsByRecipe(recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newIngredients;
    }

}
