package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.model.Friend;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/profile")
public class FeedController {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MealDao mealDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private FriendshipDao friendDao;

    @GetMapping(path = "/meal")
    public List<Meal> getMealFeed(Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> meals;
        try {
            List<Friend> friends = friendDao.getFriendList(userId);
            meals = mealDao.getMealFeed(userId, friends);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meals;
    }

    @GetMapping(path = "/meal/more/{timesLoaded}")
    public List<Meal> getMoreMeals(Principal principal, @PathVariable int timesLoaded) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Meal> meals;
        try {
            List<Friend> friends = friendDao.getFriendList(userId);
            meals = mealDao.getMoreMeals(userId, friends, timesLoaded);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return meals;
    }

    @GetMapping(path = "/recipe")
    public List<Recipe> getRecipeFeed(Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Recipe> recipes;
        try {
            List<Friend> friends = friendDao.getFriendList(userId);
            recipes = recipeDao.getRecipeFeed(userId, friends);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipes;
    }

    @GetMapping(path = "/recipe/more/{timesLoaded}")
    public List<Recipe> getMoreRecipes(Principal principal, @PathVariable int timesLoaded) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        List<Recipe> recipes;
        try {
            List<Friend> friends = friendDao.getFriendList(userId);
            recipes = recipeDao.getMoreRecipes(userId, friends, timesLoaded);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return recipes;
    }

}
