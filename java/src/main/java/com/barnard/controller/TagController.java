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

import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping(path = "/tag")
public class TagController {

    @Autowired
    private MealDao mealDao;
    @Autowired
    private TagsDao tagsDao;
    @Autowired
    private UserDao userDao;

    @GetMapping(path = "/{tagId}")
    public Tag getTag(@RequestParam int tagId) {
        Tag tag = null;
        try {
            tag = tagsDao.getTag(tagId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tag;
    }

    @GetMapping(path = "/meal/{mealId}")
    public List<Tag> getTagsByMealId(@RequestParam int mealId) {
        List<Tag> tags = null;
        try {
            tags = tagsDao.getTagsByMealId(mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tags;
    }

    @PostMapping
    public Tag createTag(@RequestBody String name) {
        Tag tag = null;
        try {
            tag = tagsDao.createTag(name);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tag;
    }

    @PutMapping
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
    public List<Tag> addTagToMeal(@RequestParam int mealId, @RequestParam int tagId) {
        List<Tag> tags = null;
        try {
            tags = tagsDao.addTagToMeal(tagId, mealId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return tags;
    }

    @DeleteMapping(path = "/meal/{mealId}/{tagId}")
    public List<Tag> deleteTagFromMeal(@RequestParam int mealId, @RequestParam int tagId) {
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
    public void deleteTag(@RequestParam int tagId) {
        try {
            tagsDao.deleteTag(tagId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }


}
