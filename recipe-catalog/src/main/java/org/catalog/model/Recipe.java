package org.catalog.model;

public class Recipe {

    private int recipeId;
    private String recipeName;
    private int avgCookTime;
    private String notes;

    public Recipe() {}

    public Recipe(int recipeId, String recipeName, int avgCookTime, String notes) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.avgCookTime = avgCookTime;
        this.notes = notes;
    }

    public int getRecipeId() {
        return recipeId;
    }
    public String getRecipeName() {
        return recipeName;
    }
    public int getAvgCookTime() {
        return avgCookTime;
    }
    public String getNotes() {
        return notes;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setAvgCookTime(int avgCookTime) {
        this.avgCookTime = avgCookTime;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

}
