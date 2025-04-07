package com.barnard.model;

import java.time.LocalDateTime;

public class UserProfile {

    private int userId;
    private String displayName;
    private boolean isPublic;
    private boolean defaultPublic;
    private Integer imageId;
    private String headline;
    private String bio;
    private String location;
    private String favoriteFoods;
    private String favoriteCuisines;
    private LocalDateTime createdAt;
    private int countMeals;
    private int countRecipes;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isDefaultPublic() {
        return defaultPublic;
    }

    public void setDefaultPublic(boolean defaultPublic) {
        this.defaultPublic = defaultPublic;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFavoriteFoods() {
        return favoriteFoods;
    }

    public void setFavoriteFoods(String favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }

    public String getFavoriteCuisines() {
        return favoriteCuisines;
    }

    public void setFavoriteCuisines(String favoriteCuisines) {
        this.favoriteCuisines = favoriteCuisines;
    }

    public int getCountMeals() {
        return countMeals;
    }

    public void setCountMeals(int countMeals) {
        this.countMeals = countMeals;
    }

    public int getCountRecipes() {
        return countRecipes;
    }

    public void setCountRecipes(int countRecipes) {
        this.countRecipes = countRecipes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
