import axios from 'axios';

export default {

  getIngredientsByMeal(mealId) {
    return axios.get('/ingredient/meal/' + mealId)
  },
  getIngredientsByRecipe(recipeId) {
    return axios.get('/ingredient/recipe/' + recipeId)
  },
  searchLikeIngredient(search) {
    return axios.get('/ingredient/search' + search)
  },
  createIngredient(ingredient) {
    return axios.post('/ingredient', ingredient)
  },
  updateIngredient(ingredient) {
    return axios.put('/ingredient', ingredient)
  },
  updateMealIngredients(ingredients) {
    return axios.put('/ingredient/meal' + ingredients)
  },
  updateRecipeIngredients(ingredients) {
    return axios.put('/ingredient/recipe' + ingredients)
  }

}