package com.barnard.dao;

import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import com.barnard.model.UserProfile;
import com.barnard.model.UserProfilePrimitive;

import java.util.List;

public interface ProfileDao {

    List<UserProfilePrimitive> searchUsers(String search, int userId);
    UserProfile getUserProfile(int userId);
    UserProfile updateUserProfile(UserProfile userProfile);
    List<Meal> getUserFeedMeals(int userId);
    List<Recipe> getUserFeedRecipes(int userId);

}
