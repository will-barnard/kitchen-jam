import axios from 'axios';

export default {

  login(user) {
    return axios.post('/login', user)
  },

  register(user) {
    return axios.post('/register', user)
  },
  requestPasswordReset(email) {
    return axios.post('/requestPasswordReset', email)
  },
  updatePassword(dto) {
    return axios.put('/updatePassword', dto)
  },
  checkUuid(uuid) {
    return axios.get('/checkUuid/' + uuid)
  }

}
