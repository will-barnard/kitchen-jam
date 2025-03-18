import axios from 'axios';

export default {

  getTag(tagId) {
    return axios.get('/tag/' + tagId)
  },
  getTagsByUser() {
    return axios.get('/tag/user')
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
  deleteTag(tagId) {
    return axios.delete('/tag/' + tagId)
  },
  getTagCategories() {
    return axios.get('/tag/categories')
  }

}