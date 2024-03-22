package com.barnard.controller;

import com.barnard.dao.ImageDao;
import com.barnard.dao.MealDao;
import com.barnard.dao.TagsDao;
import com.barnard.dao.UserDao;
import com.barnard.model.Meal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping(path = "/meal")
public class MealController {

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
        return null;
    }


}
