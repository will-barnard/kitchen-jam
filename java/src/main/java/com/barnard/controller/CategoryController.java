package com.barnard.controller;

import com.barnard.dao.CategoryDao;
import com.barnard.dao.MealDao;
import com.barnard.dao.RecipeDao;
import com.barnard.dao.UserDao;
import com.barnard.exception.AuthException;
import com.barnard.model.Category;
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
@RequestMapping(path = "/category")
public class CategoryController {

    // todo add auth

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private MealDao mealDao;

    @GetMapping(path = "/{categoryId}")
    public Category getCategory(@PathVariable int categoryId, Principal principal) {
        Category category = null;
        try {
            category = categoryDao.getCategoryById(categoryId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return category;
    }

    @GetMapping(path = "/user")
    public List<Category> getCategoriesByUser(Principal principal) {
        List<Category> categories = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            categories = categoryDao.getCategoriesByUser(userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return categories;
    }

    @PostMapping(path="")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        Category newCategory = null;
        try {
            newCategory = categoryDao.createCategory(category);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newCategory;
    }

    @PutMapping(path="")
    public Category updateCategory(@RequestBody Category category) {
        Category newCategory = null;
        try {
            newCategory = categoryDao.updateCategory(category);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newCategory;
    }

    @PostMapping(path = "/{recipeId}/{categoryId}")
    public Category addCategoryToRecipe(@PathVariable int recipeId, @PathVariable int categoryId) {
        Category category = null;
        try {
            category = categoryDao.addCategoryToRecipe(categoryId, recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return category;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{recipeId}/{categoryId}")
    public void deleteCategoryFromRecipe(@PathVariable int recipeId, @PathVariable int categoryId) {
        try {
            categoryDao.deleteCategoryFromRecipe(categoryId, recipeId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId) {
        try {
            categoryDao.deleteCategory(categoryId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }

//    private boolean verifyAuth(Principal principal, Category category) {
//        int userId = userDao.getUserByUsername(principal.getName()).getId();
//        try {
//            categories = categoryDao.getCategoriesByUser(userId);
//        } catch(Exception e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
//        }
//    }


}
