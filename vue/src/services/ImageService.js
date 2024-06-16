import axios from 'axios';

export default {

  getImage(imageId) {
    return axios.get('/image/' + imageId)
  },
  createImage(image) {
    return axios.post('/image/create', image)
  },
  deleteImage(imageId) {
    return axios.delete('/image/delete/' + imageId)
  },
  addImageToMeal(mealId, imageId) {
    return axios.post('/image/' + mealId + '/' + imageId)
  },
  addImageToRecipe(recipeId, imageId) {
    return axios.post('/image/' + recipeId + '/' + imageId)
  },
  updateMealImage(mealId, imageId) {
    return axios.put('/image/' + mealId + '/' + imageId)
  },
  updateRecipeImage(recipeId, imageId) {
    return axios.put('/image/' + recipeId + '/' + imageId)
  },
  removeMealImage(mealId, imageId) {
    return axios.delete('/image/' + mealId + '/' + imageId)
  },
  removeRecipeImage(recipeId, imageId) {
    return axios.delete('/image/' + recipeId + '/' + imageId)
  }

}