package com.barnard.dao;


import com.barnard.model.Category;

public interface CategoryDao {

    Category getCategoryById(int categoryId);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    int deleteCategory(int categoryId);

}
