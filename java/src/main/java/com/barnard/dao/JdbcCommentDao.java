package com.barnard.dao;

import com.barnard.exception.DaoException;
import com.barnard.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCommentDao implements CommentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Comment getComment(int commentId) {
        Comment comment = null;
        String sql = "SELECT comments.*, user_attributes.display_name AS user_name, " +
                     "meal.public_url AS meal_url, recipe.public_url AS recipe_url " +
                     "FROM comments " +
                     "LEFT JOIN meal ON comments.meal_id = meal.meal_id " +
                     "LEFT JOIN recipe ON comments.recipe_id = recipe.recipe_id " +
                     "JOIN user_attributes ON comments.user_id = user_attributes.user_id " +
                     "WHERE comment_id = ?";
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
        String sql = "SELECT comments.*, user_attributes.display_name AS user_name, " +
                     "meal.public_url AS meal_url " +
                     "FROM comments " +
                     "LEFT JOIN meal ON comments.meal_id = meal.meal_id " +
                     "JOIN user_attributes ON comments.user_id = user_attributes.user_id " +
                     "WHERE comments.meal_id = ? " +
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
        String sql = "SELECT comments.*, user_attributes.display_name AS user_name, " +
                     "recipe.public_url AS recipe_url " +
                     "FROM comments " +
                     "LEFT JOIN recipe ON comments.recipe_id = recipe.recipe_id " +
                     "JOIN user_attributes ON comments.user_id = user_attributes.user_id " +
                     "WHERE comments.recipe_id = ? " +
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
                    "VALUES (?, ?, ?, ?) RETURNING comment_id;";
        } else {
            sql = "INSERT INTO comments (meal_id, user_id, comment_content) " +
                    "VALUES (?, ?, ?) RETURNING comment_id;";
        }

        try {
            // Use the appropriate SQL statement based on whether it's a parent comment or not
            int commentId;
            if (isParentComment) {
                commentId = jdbcTemplate.queryForObject(sql, int.class,
                        comment.getMealId(),
                        comment.getUserId(),
                        comment.getCommentContent(),
                        comment.getParentId()
                );
            } else {
                commentId = jdbcTemplate.queryForObject(sql, int.class,
                        comment.getMealId(),
                        comment.getUserId(),
                        comment.getCommentContent()
                );
            }
            newComment = getComment(commentId);
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
                    "VALUES (?, ?, ?, ?) RETURNING comment_id;";
        } else {
            sql = "INSERT INTO comments (recipe_id, user_id, comment_content) " +
                    "VALUES (?, ?, ?) RETURNING comment_id;";
        }

        try {
            // Use the appropriate SQL statement based on whether it's a parent comment or not
            int commentId;
            if (isParentComment) {
                commentId = jdbcTemplate.queryForObject(sql, int.class,
                        comment.getRecipeId(),
                        comment.getUserId(),
                        comment.getCommentContent(),
                        comment.getParentId()
                );
            } else {
                commentId = jdbcTemplate.queryForObject(sql, int.class,
                        comment.getRecipeId(),
                        comment.getUserId(),
                        comment.getCommentContent()
                );
            }
            newComment = getComment(commentId);
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
                     "WHERE comment_id = ?";
        try {
            jdbcTemplate.update(sql, comment.getCommentContent(), comment.getCommentId());
            updatedComment = getComment(comment.getCommentId());
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
        Comment comment = new Comment();

        comment.setCommentId(rs.getInt("comment_id"));
        comment.setMealId(rs.getInt("meal_id"));
        comment.setRecipeId(rs.getInt("recipe_id"));
        comment.setParentId(rs.getInt("parent_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setCommentContent(rs.getString("comment_content"));
        if (rs.getString("created_at") != null) {
            comment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getString("updated_at") != null) {
            comment.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        try {
            comment.setMealUrl(rs.getString("meal_url") != null ? rs.getString("meal_url") : null);
        } catch(Exception e) {
            // do nothing
        }
        try {
            comment.setRecipeUrl(rs.getString("recipe_url") != null ? rs.getString("recipe_url") : null);
        } catch(Exception e) {
            // do nothing
        }
        comment.setUserName(rs.getString("user_name"));

        return comment;
    }

}
