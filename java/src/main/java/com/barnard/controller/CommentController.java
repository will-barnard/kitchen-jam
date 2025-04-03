package com.barnard.controller;

import com.barnard.dao.*;
import com.barnard.exception.DaoException;
import com.barnard.model.Comment;
import com.barnard.model.Meal;
import com.barnard.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
@RequestMapping(path = "/comment")
public class CommentController {

    @Autowired
    private MealDao mealDao;
    @Autowired
    private RecipeDao recipeDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FriendshipDao friendDao;

    @GetMapping("/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable int commentId) {
        if (commentId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Comment comment = commentDao.getComment(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        } else if (comment.getMealId() != 0 && comment.getMealId() != null) {
            if (!mealDao.isMealPublic(comment.getMealId())) {
                return ResponseEntity.status(403).build();
            }
        } else if (comment.getRecipeId() != 0 && comment.getRecipeId() != null) {
            if (!recipeDao.isRecipePublic(comment.getRecipeId())) {
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/meal/{mealId}")
    public ResponseEntity<List<Comment>> getCommentsByMeal(@PathVariable int mealId) {
        if (mealDao.isMealPublic(mealId)) {
            return ResponseEntity.ok(commentDao.getCommentsByMeal(mealId));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<List<Comment>> getCommentsByRecipe(@PathVariable int recipeId) {
        if (recipeDao.isRecipePublic(recipeId)) {
            return ResponseEntity.ok(commentDao.getCommentsByRecipe(recipeId));
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment, Principal principal) {
        Comment newComment = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (comment.getMealId() != 0 && comment.getMealId() != null) {
                if (!mealDao.isMealPublic(comment.getMealId())) {
                    return ResponseEntity.status(403).build();
                } else {
                    Meal meal = mealDao.getMeal(comment.getMealId());
                    if (meal.getUserId() == userId) {
                        newComment = commentDao.createComment(comment);
                    } else if (friendDao.isFriend(userId, meal.getUserId())) {
                        newComment = commentDao.createComment(comment);
                    } else {
                        return ResponseEntity.status(403).build();
                    }
                }
            } else if (comment.getRecipeId() != 0 && comment.getRecipeId() != null) {
                if (!recipeDao.isRecipePublic(comment.getRecipeId())) {
                    return ResponseEntity.status(403).build();
                } else {
                    Recipe recipe = recipeDao.getRecipe(comment.getRecipeId());
                    if (recipe.getUserId() == userId) {
                        newComment = commentDao.createComment(comment);
                    } else if (friendDao.isFriend(userId, recipe.getUserId())) {
                        newComment = commentDao.createComment(comment);
                    } else {
                        return ResponseEntity.status(403).build();
                    }
                }
            } else {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(newComment);
        } catch (DaoException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<Comment> updateComment(@RequestBody Comment comment, Principal principal) {
        Comment updatedComment = null;
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            if (!commentDao.verifyCommentOwner(userId, comment.getCommentId())) {
                return ResponseEntity.status(403).build();
            } else {
                updatedComment = commentDao.updateComment(comment);
            }
        } catch (DaoException e) {
            return ResponseEntity.badRequest().body(null);
        }
        return updatedComment != null ? ResponseEntity.ok(updatedComment) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        try {
            Comment comment = commentDao.getComment(commentId);
            if (!commentDao.verifyCommentOwner(userId, commentId)) {
                if (comment.getMealId() != 0 && comment.getMealId() != null) {
                    if (mealDao.verifyMealOwner(userId, comment.getMealId())) {
                        int rowsAffected = commentDao.deleteComment(commentId);
                        return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
                    }
                } else if (comment.getRecipeId() != 0 && comment.getRecipeId() != null) {
                    if (recipeDao.verifyRecipeOwner(userId, comment.getRecipeId())) {
                        int rowsAffected = commentDao.deleteComment(commentId);
                        return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
                    }
                }
                return ResponseEntity.status(403).build();
            } else {
                int rowsAffected = commentDao.deleteComment(commentId);
                return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
            }
        } catch (DaoException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/meal/{mealId}")
    public ResponseEntity<Void> deleteCommentsByMeal(@PathVariable int mealId, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        if (!mealDao.verifyMealOwner(userId, mealId)) {
            return ResponseEntity.status(403).build();
        }
        int rowsAffected = commentDao.deleteCommentsByMeal(mealId);
        return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<Void> deleteCommentsByRecipe(@PathVariable int recipeId, Principal principal) {
        int userId = userDao.getUserByUsername(principal.getName()).getId();
        if (!recipeDao.verifyRecipeOwner(userId, recipeId)) {
            return ResponseEntity.status(403).build();
        }
        int rowsAffected = commentDao.deleteCommentsByRecipe(recipeId);
        return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteCommentsByUser(@PathVariable int userId, Principal principal) {
        int currentUserId = userDao.getUserByUsername(principal.getName()).getId();
        if (currentUserId != userId) {
            return ResponseEntity.status(403).build();
        }
        int rowsAffected = commentDao.deleteCommentsByUser(userId);
        return rowsAffected > 0 ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
