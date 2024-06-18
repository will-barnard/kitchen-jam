import axios from 'axios';

export default {

  getTag(tagId) {
    return axios.get('/tag/' + tagId)
  },
  searchTags(search) {
    return axios.post('/tag/search', search)
  },
  createTag(tag) {
    return axios.post('/tag', tag)
  },
  updateTag(tag) {
    return axios.put('/tag', tag)
  },
  addTagToMeal(mealId, tagId) {
    return axios.post('/tag/meal/' + mealId + '/' + tagId)
  },
  removeTagFromMeal(mealId, tagId) {
    return axios.delete('/tag/meal/' + mealId + '/' + tagId)
  }

}