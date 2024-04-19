import axios from 'axios';

export default {

  getMeal(mealId) {
    return axios.get('/meal/' + mealId)
  },
  getMealsByUser() {
    return axios.get('/meal/user')
  },
  createMeal(meal) {
    return axios.post('/meal', meal)
  },
  updateMeal(meal) {
    return axios.put('/meal/update', meal)
  },
  deleteMeal(mealId) {
    return axios.delete('/meal/' + mealId)
  }

  
}