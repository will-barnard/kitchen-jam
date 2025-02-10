package com.barnard.controller;

import com.barnard.dao.ProfileDao;
import com.barnard.dao.UserDao;
import com.barnard.exception.DaoException;
import com.barnard.model.UserFeedDto;
import com.barnard.model.UserProfile;
import com.barnard.model.UserProfilePrimitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping(path = "/profile")
public class ProfileController {

    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private UserDao userDao;

    @PostMapping(path = "/search")
    public List<UserProfilePrimitive> searchUsers(@RequestBody String search) {
        List<UserProfilePrimitive> result;
        try {
            result = profileDao.searchUsers(search);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return result;
    }

    @GetMapping(path = "/{userId}")
    public UserProfile getUserProfile(@PathVariable int userId) {
        UserProfile profile;
        try {
            profile = profileDao.getUserProfile(userId);
            if (!profile.isPublic()) {
                throw new Exception("profile not public");
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return profile;
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/update")
    public UserProfile updateUserProfile(@RequestBody UserProfile userProfile, Principal principal) {
        UserProfile updatedProfile;
        try {
            int userId = userDao.getUserByUsername(principal.getName()).getId();
            if (userId != userProfile.getUserId()) {
                throw new Exception("not authorized");
            } else {
                updatedProfile = profileDao.updateUserProfile(userProfile);
            }
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return updatedProfile;
    }

    @GetMapping(path = "/feed/{userId}")
    public UserFeedDto getUserFeed(@PathVariable int userId) {
        UserFeedDto feed = new UserFeedDto();
        try {
            feed.setMeals(profileDao.getUserFeedMeals(userId));
            feed.setRecipes(profileDao.getUserFeedRecipes(userId));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return feed;
    }


}
