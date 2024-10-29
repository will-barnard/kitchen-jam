import axios from 'axios';

export default {

  getMeal(mealId) {
    return axios.get('/meal/' + mealId)
  },
  getPublicMeal(uuid) {
    return axios.get('/meal/public/' + uuid)
  },
  getMealsByUser() {
    return axios.get('/meal/user')
  },
  createMeal(meal) {
    return axios.post('/meal', meal)
  },
  updateMeal(meal) {
    return axios.put('/meal', meal)
  },
  makePublic(mealId) {
    return axios.post('/meal/public/' + mealId)
  },
  makePrivate(mealId) {
    return axios.delete('/meal/public/' + mealId)
  },
  deleteMeal(mealId) {
    return axios.delete('/meal/' + mealId)
  }

}