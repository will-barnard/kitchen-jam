package org.catalog.model;

import java.time.LocalDate;

public class Meal {

    private int mealId;
    private int recipeId;
    private LocalDate dateCreated;
    private int cookTime;
    private String notes;
    private String ingredientsUsed;
    private int rating;

    public Meal() {}

    public Meal(int mealId, int recipeId, LocalDate dateCreated, int cookTime, String notes, String ingredientsUsed, int rating) {
        this.mealId = mealId;
        this.recipeId = recipeId;
        this.dateCreated = dateCreated;
        this.cookTime = cookTime;
        this.notes = notes;
        this.ingredientsUsed = ingredientsUsed;
        this.rating = rating;
    }

    public int getMealId() {
        return mealId;
    }
    public int getRecipeId() {
        return recipeId;
    }
    public LocalDate getDateCreated() {
        return dateCreated;
    }
    public int getCookTime() {
        return cookTime;
    }
    public String getNotes() {
        return notes;
    }
    public String getIngredientsUsed() {
        return ingredientsUsed;
    }
    public int getRating() {
        return rating;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public void setIngredientsUsed(String ingredientsUsed) {
        this.ingredientsUsed = ingredientsUsed;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

}
