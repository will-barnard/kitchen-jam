import axios from 'axios';

export default {
    
  getComment(commentId) {
    return axios.get(`/comment/${commentId}`);
  },

  getCommentsByMeal(mealId) {
    return axios.get(`/comment/meal/${mealId}`);
  },

  getCommentsByRecipe(recipeId) {
    return axios.get(`/comment/recipe/${recipeId}`);
  },

  createComment(comment) {
    return axios.post('/comment', comment);
  },

  updateComment(comment) {
    return axios.put('/comment', comment);
  },

  deleteComment(commentId) {
    return axios.delete(`/comment/${commentId}`);
  },

  deleteCommentsByMeal(mealId) {
    return axios.delete(`/comment/meal/${mealId}`);
  },

  deleteCommentsByRecipe(recipeId) {
    return axios.delete(`/comment/recipe/${recipeId}`);
  },

  deleteCommentsByUser(userId) {
    return axios.delete(`/comment/user/${userId}`);
  }

};