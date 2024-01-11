package com.barnard.recipecatalog.dao;

import com.barnard.recipecatalog.model.Category;

public interface CategoryDao {

    Category getCategoryById(int categoryId);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    int deleteCategory(int categoryId);

}
