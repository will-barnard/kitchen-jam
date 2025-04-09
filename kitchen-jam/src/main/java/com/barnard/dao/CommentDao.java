package com.barnard.dao;

import com.barnard.model.Comment;
import com.barnard.model.UserAttributes;

import java.util.List;

public interface CommentDao {

    Comment getComment(int commentId);
    List<Comment> getCommentsByMeal(int mealId);
    List<Comment> getCommentsByRecipe(int recipeId);
    List<UserAttributes> getCommentersByMeal(int mealId);
    List<UserAttributes> getCommentersByRecipe(int recipeId);
    Comment createComment(Comment comment);
    Comment updateComment(Comment comment);
    int deleteComment(int commentId);
    int deleteCommentsByMeal(int mealId);
    int deleteCommentsByRecipe(int recipeId);
    int deleteCommentsByUser(int userId);
    boolean verifyCommentOwner(int userId, int commentId);

}
