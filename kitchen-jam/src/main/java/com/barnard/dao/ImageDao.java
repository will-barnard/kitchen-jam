package com.barnard.dao;


import com.barnard.model.Image;

public interface ImageDao {

    int createImage(String imagePath);
    String getImagePathById(int imageId);
    String updateImage(String imagePath, int imageId);
    String deleteImageByImageId(int imageId);
    void addImageToMeal(int mealId, int imageId);
    void addImageToRecipe(int recipeId, int imageId);
    void addImageToProfile(int userId, int imageId);
    void updateMealImage(int mealId, int imageId);
    void updateRecipeImage(int recipeId, int imageId);
    void updateProfileImage(int userId, int imageId);
    void removeImageFromMeal(int mealId);
    void removeImageFromRecipe(int recipeId);
    void removeImageFromProfile(int userId);

}
