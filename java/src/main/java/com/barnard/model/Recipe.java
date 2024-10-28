package com.barnard.model;

import java.util.List;

public class Recipe {

    private Integer recipeId;
    private int userId;
    private String recipeName;
    private Integer avgCookTime;
    private String description;
    private Integer imageId;
    private boolean isPublic;
    private Integer categoryId;
    private String categoryName;
    private Category category;
    private List<Meal> mealList;
    private List<Step> stepList;
    private boolean updateSteps = false;

    public Integer getRecipeId() {
        return recipeId;
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

    public Integer getAvgCookTime() {
        return avgCookTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public void setAvgCookTime(Integer avgCookTime) {
        this.avgCookTime = avgCookTime;
    }

    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Step> getStepList() {
        return stepList;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public boolean isUpdateSteps() {
        return updateSteps;
    }

    public void setUpdateSteps(boolean updateSteps) {
        this.updateSteps = updateSteps;
    }
}
