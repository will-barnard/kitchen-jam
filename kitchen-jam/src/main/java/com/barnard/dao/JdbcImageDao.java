package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcImageDao implements ImageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int createImage(String imagePath) {
        int imageId;
        String sql = "INSERT into image (image_path) " +
                "VALUES (?) " +
                "RETURNING image_id;";
        try {
            imageId = jdbcTemplate.queryForObject(sql, int.class, imagePath);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return imageId;
    }

    @Override
    public String getImagePathById(int imageId) {
        String path = null;
        String sql = "SELECT image_path FROM image " +
                "WHERE image_id = ?;";
        try {
            path = jdbcTemplate.queryForObject(sql, String.class, imageId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return path;
    }

    @Override
    public String updateImage(String imagePath, int imageId) {
        String sql = "UPDATE image SET image_path = ? " +
                "WHERE image_id = ?;";
        try {
            jdbcTemplate.update(sql, imagePath, imageId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return imagePath;
    }

    @Override
    public String deleteImageByImageId(int imageId) {
        String path = null;
        String sql1 = "SELECT image_path FROM image " +
                "WHERE image_id = ?;";
        String sql2 = "DELETE FROM meal_image WHERE image_id = ?; " +
                "DELETE FROM recipe_image WHERE image_id = ?; " +
                "DELETE FROM image WHERE image_id = ?;";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql1, imageId);
            if (rs.next()) {
                path = rs.getString("image_path");
            }
            jdbcTemplate.update(sql2, imageId, imageId, imageId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return path;
    }

    @Override
    public void addImageToMeal(int mealId, int imageId) {
        String sql = "UPDATE meal " +
                "SET image_id = ? " +
                "WHERE meal_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void addImageToRecipe(int recipeId, int imageId) {
        String sql = "UPDATE recipe " +
                "SET image_id = ? " +
                "WHERE recipe_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void addImageToProfile(int userId, int imageId) {
        String sql = "UPDATE user_attributes " +
                "SET image_id = ? " +
                "WHERE user_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void updateMealImage(int mealId, int imageId) {
        String sql = "UPDATE meal " +
                "SET image_id = ? " +
                "WHERE meal_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void updateRecipeImage(int recipeId, int imageId) {
        String sql = "UPDATE recipe " +
                "SET image_id = ? " +
                "WHERE recipe_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void updateProfileImage(int userId, int imageId) {
        String sql = "UPDATE user_attributes " +
                "SET image_id = ? " +
                "WHERE user_id = ?;";
        try {
            jdbcTemplate.update(sql, imageId, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void removeImageFromMeal(int mealId) {
        String sql = "UPDATE meal " +
                "SET image_id = NULL " +
                "WHERE meal_id = ?;";
        try {
            jdbcTemplate.update(sql, mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void removeImageFromRecipe(int recipeId) {
        String sql = "UPDATE recipe " +
                "SET image_id = NULL " +
                "WHERE recipe_id = ?;";
        try {
            jdbcTemplate.update(sql, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public void removeImageFromProfile(int userId) {
        String sql = "UPDATE user_attributes " +
                "SET image_id = NULL " +
                "WHERE user_id = ?;";
        try {
            jdbcTemplate.update(sql, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

}
