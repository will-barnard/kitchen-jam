package com.barnard.controller;

import com.barnard.dao.MealDao;
import com.barnard.dao.ProfileDao;
import com.barnard.dao.RecipeDao;
import com.barnard.dao.UserDao;
import com.barnard.model.ProfileLogDto;
import com.barnard.model.UserProfile;
import com.barnard.model.UserProfilePrimitive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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
    @Autowired
    private MealDao mealDao;
    @Autowired
    private RecipeDao recipeDao;

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
    @GetMapping(path = "/principal")
    public UserProfile getPrincipalUserProfile(Principal principal) {
        UserProfile profile;
        try {
            int userId = userDao.getUserByUsername(principal.getName()).getId();
            profile = profileDao.getUserProfile(userId);
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

    @PostMapping(path = "/search")
    public List<UserProfilePrimitive> searchUsers(@RequestBody String search, Principal principal) {
        List<UserProfilePrimitive> result;
        String decodedSearch = search.substring(0, search.length() - 1);
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            result = profileDao.searchUsers(decodedSearch, userId);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return result;
    }

    @GetMapping(path = "/log/{userId}")
    public ProfileLogDto getUserProfileLog(@PathVariable int userId) {
        ProfileLogDto profileLog = new ProfileLogDto();
        try {
            if (!profileDao.isProfilePublic(userId)) {
                throw new Exception("profile not public");
            }
            profileLog.setUserId(userId);
            profileLog.setMeals(mealDao.getUserProfileMeals(userId));
            profileLog.setRecipes(recipeDao.getUserProfileRecipes(userId));
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
        return profileLog;
    }




}
