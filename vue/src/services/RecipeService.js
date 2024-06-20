import axios from "axios";

export default {

    getRecipe(recipeId) {
      return axios.get('/recipe/' + recipeId)
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
    deleteRecipe(recipeId) {
      return axios.delete('/recipe/' + recipeId)
    }

}