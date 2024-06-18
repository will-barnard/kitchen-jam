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
    return axios.post('/image/meal/' + mealId + '/' + imageId, {
      responseType: 'arraybuffer'
    })
  },
  addImageToRecipe(recipeId, imageId) {
    return axios.post('/image/recipe/' + recipeId + '/' + imageId, {
      responseType: 'arraybuffer'
    })
  },
  updateMealImage(mealId, imageId) {
    return axios.put('/image/meal/' + mealId + '/' + imageId, {
      responseType: 'arraybuffer'
    })
  },
  updateRecipeImage(recipeId, imageId) {
    return axios.put('/image/recipe/' + recipeId + '/' + imageId, {
      responseType: 'arraybuffer'
    })
  },
  removeMealImage(mealId, imageId) {
    return axios.delete('/image/meal/' + mealId + '/' + imageId)
  },
  removeRecipeImage(recipeId, imageId) {
    return axios.delete('/image/recipe/' + recipeId + '/' + imageId)
  },
  parseImg(res) {
    const base64 = btoa(
      new Uint8Array(res.data).reduce(
        (data, byte) => data + String.fromCharCode(byte),
        ''
      )
    );
    return base64;
  }

}