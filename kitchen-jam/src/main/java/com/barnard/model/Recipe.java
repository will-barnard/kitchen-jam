package com.barnard.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Recipe {

    private Integer recipeId;
    private int userId;
    private String userName;
    private String recipeName;
    private BigDecimal avgCookTime;
    private BigDecimal avgRating;
    private String description;
    private Integer imageId;
    private boolean isPublic;
    private Integer categoryId;
    private String categoryName;
    private Category category;
    private LocalDate lastCreated;
    private List<Meal> mealList;
    private List<Step> stepList;
    private boolean updateSteps = false;
    private List<Ingredient> ingredientList;
    private String publicUrl;

    public Integer getRecipeId() {
        return recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public BigDecimal getAvgCookTime() {
        return avgCookTime;
    }

    public BigDecimal getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(BigDecimal avgRating) {
        this.avgRating = avgRating;
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

    public void setAvgCookTime(BigDecimal avgCookTime) {
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

    public String getPublicUrl() {
        return publicUrl;
    }

    public void setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public LocalDate getLastCreated() {
        return lastCreated;
    }

    public void setLastCreated(LocalDate lastCreated) {
        this.lastCreated = lastCreated;
    }
}
