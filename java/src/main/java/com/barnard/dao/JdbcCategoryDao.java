package com.barnard.dao;


import com.barnard.exception.DaoException;
import com.barnard.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao{

    // todo add authentication and security when doing GET

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Category getCategoryById(int categoryId) {

        Category category = null;
        String sql = "SELECT * " +
                "FROM category " +
                "WHERE category_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, categoryId);
            if (rowSet.next()) {
                category = mapRowToCategory(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return category;
    }

    @Override
    public List<Category> getCategoriesByUser(int userId) {

        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * " +
                "FROM category " +
                "WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while(rowSet.next()) {
                categories.add(mapRowToCategory(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return categories;
    }

    @Override
    public List<Category> searchLikeCategory(String search, int userId) {
        List<Category> categories = new ArrayList<>();
        search = "%" + search.toLowerCase() + "%";
        String sql = "SELECT * " +
                "FROM category " +
                "WHERE LOWER (category_name) LIKE ? " +
                "AND user_id = ?;";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, search, userId);
            while (rowSet.next()) {
                categories.add(mapRowToCategory(rowSet));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return categories;
    }

    @Override
    public Category createCategory(Category category, int userId) {

        Category newCategory = null;
        String sql = "INSERT INTO category (category_name, user_id) " +
                "VALUES (?, ?) " +
                "RETURNING category_id;";

        try {
            int categoryId = jdbcTemplate.queryForObject(sql, int.class, category.getCategoryName(), userId);
            newCategory = getCategoryById(categoryId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newCategory;
    }

    @Override
    public Category updateCategory(Category category) {

        Category newCategory = null;
        String sql = "UPDATE category SET category_name = ? " +
                "WHERE category_id = ?;";

        try {
            jdbcTemplate.update(sql, category.getCategoryName(), category.getCategoryId());
            newCategory = getCategoryById(category.getCategoryId());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return newCategory;
    }

    @Override
    public void deleteCategory(int categoryId) {

        String sql = "UPDATE recipe SET category_id = null " +
                "WHERE category_id = ?;" +
                "DELETE FROM category " +
                "WHERE category_id = ?;";

        try {
            jdbcTemplate.update(sql, categoryId, categoryId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private Category mapRowToCategory(SqlRowSet rs) {
        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setUserId(rs.getInt("user_id"));

        return category;
    }
}
