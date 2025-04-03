package com.barnard.model;

public class Comment {

    private Integer commentId;
    private Integer mealId;
    private String mealUrl;
    private Integer recipeId;
    private String recipeUrl;
    private Integer parentId;
    private Integer userId;
    private String userName;
    private String commentContent;
    private String createdAt;
    private String updatedAt;

    public Comment() {
    }

    public Comment(Integer commentId, Integer mealId, Integer recipeId, Integer parentId, Integer userId, String commentContent, String createdAt, String updatedAt, String mealUrl, String recipeUrl) {
        this.commentId = commentId;
        this.mealId = mealId;
        this.recipeId = recipeId;
        this.parentId = parentId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.mealUrl = mealUrl;
        this.recipeUrl = recipeUrl;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getMealId() {
        return mealId;
    }

    public void setMealId(Integer mealId) {
        this.mealId = mealId;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getMealUrl() {
        return mealUrl;
    }

    public void setMealUrl(String mealUrl) {
        this.mealUrl = mealUrl;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }
}
