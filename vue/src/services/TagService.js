import axios from 'axios';

export default {

  getTag(tagId) {
    return axios.get('/tag/' + tagId)
  },
  createTag(tag) {
    return axios.post('/tag', tag)
  },
  updateTag(tag) {
    return axios.put('/tag', tag)
  },
  addTagToMeal(mealId, tagId) {
    return axios.post('/tag/meal/' + mealId + '/' + tagId)
  }

}