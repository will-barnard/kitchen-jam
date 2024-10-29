import axios from "axios";

export default {

    getRecipe(recipeId) {
      return axios.get('/recipe/' + recipeId)
    },
    getPublicRecipe(uuid) {
      return axios.get('/recipe/public/' + uuid)
    },
    searchRecipes(search) {
      return axios.post('/recipe/search', search)
    },
    getRecipesByUser() {
      return axios.get('/recipe/user')
    },
    createRecipe(recipe) {
      return axios.post('/recipe', recipe)
    },
    updateRecipe(recipe) {
      return axios.put('/recipe', recipe)
    },
    makePublic(recipeId) {
      return axios.post('/recipe/public/' + recipeId)
    },
    makePrivate(recipeId) {
      return axios.delete('/recipe/public/' + recipeId)
    },
    deleteRecipe(recipeId) {
      return axios.delete('/recipe/' + recipeId)
    }

}