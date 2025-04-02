import axios from 'axios';

export default {

  getProfile(userId) {
    return axios.get('/profile/' + userId)
  },
  getPrincipalProfile() {
    return axios.get('/profile/principal')
  },
  updateProfile(profile) {
    return axios.put('/profile/update', profile)
  },
  searchUsers(search) {
    return axios.post('/profile/search', search)
  },
  getUserProfileLog(userId) {
    return axios.get('/profile/log/' + userId)
  },

}