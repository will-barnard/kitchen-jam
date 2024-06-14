package com.barnard.model;

import org.springframework.data.relational.core.sql.In;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Meal {

    private int mealId;
    private int userId;
    private Integer recipeId;
    private String mealName;
    private String mealComment;
    private LocalDate dateCooked;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
    private Integer cookTime;
    private String notes;
    private String ingredients;
    private Integer rating;
    private Integer imageId;
    private boolean isPublic;
    private List<Tag> tags;

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealComment() {
        return mealComment;
    }

    public void setMealComment(String mealComment) {
        this.mealComment = mealComment;
    }

    public LocalDate getDateCooked() {
        return dateCooked;
    }

    public void setDateCooked(LocalDate dateCooked) {
        this.dateCooked = dateCooked;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
