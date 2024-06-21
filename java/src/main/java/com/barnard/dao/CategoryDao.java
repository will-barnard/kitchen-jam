package com.barnard.dao;


import com.barnard.model.Category;

import java.util.List;

public interface CategoryDao {

    Category getCategoryById(int categoryId);
    List<Category> getCategoriesByUser(int userId);
    List<Category> searchLikeCategory(String search, int userId);
    Category createCategory(Category category, int userId);
    Category updateCategory(Category category);
    Category addCategoryToRecipe(int categoryId, int recipeId);
    void deleteCategoryFromRecipe(int categoryId, int recipeId);
    void deleteCategory(int categoryId);

}
