package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.model.Meal;
import com.barnard.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("permitAll")
@RequestMapping(path = "/will")
public class WillController {

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

    @GetMapping(path = "")
    public List<Meal> getMealsByUser(Principal principal) {
        // Will's account ID
        int userId = 1001;
        List<Meal> result = mealDao.getMealsByUserId(userId);
        for (Meal meal : result) {
            meal.setTags(tagsDao.getTagsByMealId(meal.getMealId()));
        }
        return result;
    }


}
