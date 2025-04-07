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
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            category = categoryDao.getCategoryById(categoryId);
            if (category.getUserId() != userId) {
                throw new AuthException("Unauthorized");
            }
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

    @PostMapping(path = "/search")
    public List<Category> searchCategory(@RequestBody Category category, Principal principal) {
        List<Category> categories = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            categories = categoryDao.searchLikeCategory(category.getCategoryName(), userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return categories;
    }

    @PostMapping(path="")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category, Principal principal) {
        Category newCategory = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            newCategory = categoryDao.createCategory(category, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newCategory;
    }

    @PutMapping(path="")
    public Category updateCategory(@RequestBody Category category, Principal principal) {
        Category newCategory = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (userId != categoryDao.getCategoryById(category.getCategoryId()).getUserId()) {
                throw new AuthException("Unauthorized");
            } else {
                newCategory = categoryDao.updateCategory(category);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newCategory;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (userId != categoryDao.getCategoryById(categoryId).getUserId()) {
                throw new AuthException("Unauthorized");
            } else {
                categoryDao.deleteCategory(categoryId);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }
}
