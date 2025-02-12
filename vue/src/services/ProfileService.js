import axios from 'axios';

export default {

  getProfile(userId) {
    return axios.get('/profile/' + userId)
  },
  getPrincipalProfile() {
    return axios.get('/profile/principal')
  },
  getUserFeed(userId) {
    return axios.get('/profile/feed/' + userId)
  },
  updateProfile(profile) {
    return axios.put('/profile/update', profile)
  },
  searchUsers(search) {
    return axios.post('/profile/search', search)
  }

}