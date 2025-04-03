package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Comment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCommentDao implements CommentDao {

    private JdbcTemplate jdbcTemplate;

//    CREATE TABLE comments (
//            comment_id SERIAL PRIMARY KEY,
//            meal_id INT REFERENCES meal(meal_id) ON DELETE CASCADE,
//    recipe_id INT REFERENCES recipe(recipe_id) ON DELETE CASCADE,
//    parent_id INT REFERENCES comments(comment_id) ON DELETE CASCADE,
//    user_id INT NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
//    comment_content TEXT NOT NULL,
//    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
//);

    @Override
    public Comment getComment(int commentId) {
        Comment comment = null;
        String sql = "SELECT * FROM comments WHERE comment_id = ?";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, commentId);
            if (rs.next()) {
                comment = mapRowToComment(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return comment;

    }

    @Override
    public List<Comment> getCommentsByMeal(int mealId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE meal_id = ? " +
                "ORDER BY created_at DESC";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, mealId);
            while (rs.next()) {
                Comment comment = mapRowToComment(rs);
                comments.add(comment);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentsByRecipe(int recipeId) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE recipe_id = ? " +
                "ORDER BY created_at DESC";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, recipeId);
            while (rs.next()) {
                Comment comment = mapRowToComment(rs);
                comments.add(comment);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return comments;
    }

    @Override
    public Comment createComment(Comment comment) {
        Comment newComment = null;
        if (comment.getMealId() == null && comment.getRecipeId() == null) {
            throw new DaoException("Meal ID or Recipe ID must be provided");
        } else if (comment.getMealId() != null && comment.getRecipeId() != null) {
            throw new DaoException("Only one of Meal ID or Recipe ID must be provided");
        } else if (comment.getMealId() != null) {
            newComment = createMealComment(comment);
        } else {
            newComment = createRecipeComment(comment);
        }
        return newComment;
    }

    private Comment createMealComment(Comment comment) {
        Comment newComment = null;
        String sql;
        boolean isParentComment = comment.getParentId() != null && comment.getParentId() != 0;
        if (isParentComment) {
            sql = "INSERT INTO comments (meal_id, user_id, comment_content, parent_id) " +
                    "VALUES (?, ?, ?, ?) RETURNING *";
        } else {
            sql = "INSERT INTO comments (meal_id, user_id, comment_content) " +
                    "VALUES (?, ?, ?) RETURNING *";
        }

        try {
            // Use the appropriate SQL statement based on whether it's a parent comment or not
            SqlRowSet rs;
            if (isParentComment) {
                rs = jdbcTemplate.queryForRowSet(sql,
                        comment.getMealId(),
                        comment.getUserId(),
                        comment.getCommentContent(),
                        comment.getParentId()
                );
            } else {
                rs = jdbcTemplate.queryForRowSet(sql,
                        comment.getMealId(),
                        comment.getUserId(),
                        comment.getCommentContent()
                );
            }
            if (rs.next()) {
                newComment = mapRowToComment(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newComment;
    }

    private Comment createRecipeComment(Comment comment) {
        Comment newComment = null;
        String sql;
        boolean isParentComment = comment.getParentId() != null && comment.getParentId() != 0;
        if (isParentComment) {
            sql = "INSERT INTO comments (recipe_id, user_id, comment_content, parent_id) " +
                    "VALUES (?, ?, ?, ?) RETURNING *";
        } else {
            sql = "INSERT INTO comments (recipe_id, user_id, comment_content) " +
                    "VALUES (?, ?, ?) RETURNING *";
        }

        try {
            // Use the appropriate SQL statement based on whether it's a parent comment or not
            SqlRowSet rs;
            if (isParentComment) {
                rs = jdbcTemplate.queryForRowSet(sql,
                        comment.getRecipeId(),
                        comment.getUserId(),
                        comment.getCommentContent(),
                        comment.getParentId()
                );
            } else {
                rs = jdbcTemplate.queryForRowSet(sql,
                        comment.getRecipeId(),
                        comment.getUserId(),
                        comment.getCommentContent()
                );
            }
            if (rs.next()) {
                newComment = mapRowToComment(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newComment;
    }

    @Override
    public Comment updateComment(Comment comment) {
        Comment updatedComment = null;
        String sql = "UPDATE comments SET comment_content = ?, updated_at = CURRENT_TIMESTAMP " +
                     "WHERE comment_id = ? RETURNING *";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, comment.getCommentContent(), comment.getCommentId());
            if (rs.next()) {
                updatedComment = mapRowToComment(rs);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return updatedComment;
    }

    @Override
    public int deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        try {
            return jdbcTemplate.update(sql, commentId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteCommentsByMeal(int mealId) {
        String sql = "DELETE FROM comments WHERE meal_id = ?";
        try {
            return jdbcTemplate.update(sql, mealId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteCommentsByRecipe(int recipeId) {
        String sql = "DELETE FROM comments WHERE recipe_id = ?";
        try {
            return jdbcTemplate.update(sql, recipeId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public int deleteCommentsByUser(int userId) {
        String sql = "DELETE FROM comments WHERE user_id = ?";
        try {
            return jdbcTemplate.update(sql, userId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public boolean verifyCommentOwner(int userId, int commentId) {
        boolean result = false;
        String sql = "SELECT * FROM comments WHERE comment_id = ? AND user_id = ?";
        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, commentId, userId);
            if (rs.next()) {
                result = true;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return result;
    }

    private Comment mapRowToComment(SqlRowSet rs) {
        return new Comment(
                rs.getInt("comment_id"),
                rs.getInt("meal_id"),
                rs.getInt("recipe_id"),
                rs.getInt("parent_id"),
                rs.getInt("user_id"),
                rs.getString("comment_content"),
                rs.getString("created_at"),
                rs.getString("updated_at"),
                rs.getString("meal_url"),
                rs.getString("recipe_url")
        );
    }

}
