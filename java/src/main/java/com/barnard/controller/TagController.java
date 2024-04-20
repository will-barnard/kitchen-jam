package com.barnard.controller;

import com.barnard.dao.MealDao;
import com.barnard.dao.TagsDao;
import com.barnard.dao.UserDao;
import com.barnard.model.Meal;
import com.barnard.model.Tag;
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
@RequestMapping(path = "/tag")
public class TagController {

    // todo add authentication and security when doing GET

    @Autowired
    private MealDao mealDao;
    @Autowired
    private TagsDao tagsDao;
    @Autowired
    private UserDao userDao;

    @GetMapping(path = "/{tagId}")
    public Tag getTag(@PathVariable int tagId) {
        Tag tag = null;
        try {
            tag = tagsDao.getTag(tagId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tag;
    }

    @GetMapping(path = "/meal/{mealId}")
    public List<Tag> getTagsByMealId(@PathVariable int mealId) {
        List<Tag> tags = null;
        try {
            tags = tagsDao.getTagsByMealId(mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tags;
    }

    @PostMapping(path="")
    @ResponseStatus(HttpStatus.CREATED)
    public Tag createTag(@RequestBody Tag tag, Principal principal) {
        Tag newTag = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        tag.setUserId(userId);
        try {
            tag = tagsDao.createTag(tag, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tag;
    }

    @PutMapping(path="")
    public Tag updateTag(@RequestBody Tag tag) {
        Tag newTag = null;
        try {
            newTag = tagsDao.updateTag(tag);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return newTag;
    }

    @PostMapping(path = "/meal/{mealId}/{tagId}")
    public List<Tag> addTagToMeal(@PathVariable int mealId, @PathVariable int tagId) {
        List<Tag> tags = null;
        try {
            tags = tagsDao.addTagToMeal(tagId, mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tags;
    }

    @DeleteMapping(path = "/meal/{mealId}/{tagId}")
    public List<Tag> deleteTagFromMeal(@PathVariable int mealId, @PathVariable int tagId) {
        List<Tag> tags = null;
        try {
            tags = tagsDao.deleteTagFromMeal(tagId, mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tags;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(path = "/{tagId}")
    public void deleteTag(@PathVariable int tagId) {
        try {
            tagsDao.deleteTag(tagId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }


}
