import axios from 'axios';

export default {

  getMeal(mealId) {
    // return axios.get()
  },
  getMealsByUser() {
    return axios.get('/meal/user')
  }

  
}