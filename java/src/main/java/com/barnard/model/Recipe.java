package com.barnard.model;

public class Recipe {

    private int recipeId;
    private int userId;
    private String recipeName;
    private int avgCookTime;
    private String description;
    private int image_id;

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getAvgCookTime() {
        return avgCookTime;
    }

    public void setAvgCookTime(int avgCookTime) {
        this.avgCookTime = avgCookTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }
}
