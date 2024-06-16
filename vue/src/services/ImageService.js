import axios from 'axios';

export default {

  getImage(imageId) {
    return axios.get('/image/' + imageId, {
      responseType: 'arraybuffer'
    })
  },
  createImage(image) {
    let data = new FormData();
    data.append('image', image);
    let config = {
      header : {
        'Content-Type' : 'multipart/form-data'
      }
    }
    return axios.post('/image/create', data, config)
  },
  deleteImage(imageId) {
    return axios.delete('/image/delete/' + imageId)
  },
  addImageToMeal(mealId, imageId) {
    return axios.post('/image/meal/' + mealId + '/' + imageId)
  },
  addImageToRecipe(recipeId, imageId) {
    return axios.post('/image/recipe/' + recipeId + '/' + imageId)
  },
  updateMealImage(mealId, imageId) {
    return axios.put('/image/meal/' + mealId + '/' + imageId)
  },
  updateRecipeImage(recipeId, imageId) {
    return axios.put('/image/recipe/' + recipeId + '/' + imageId)
  },
  removeMealImage(mealId, imageId) {
    return axios.delete('/image/meal/' + mealId + '/' + imageId)
  },
  removeRecipeImage(recipeId, imageId) {
    return axios.delete('/image/recipe/' + recipeId + '/' + imageId)
  },
  parseImg(img) {
    return 
  }

}