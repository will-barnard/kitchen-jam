package com.barnard.dao;


import com.barnard.model.Category;

import java.util.List;

public interface CategoryDao {

    Category getCategoryById(int categoryId);
    List<Category> getCategoriesByUser(int userId);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    Category addCategoryToRecipe(int categoryId, int recipeId);
    void deleteCategoryFromRecipe(int categoryId, int recipeId);
    void deleteCategory(int categoryId);

}
