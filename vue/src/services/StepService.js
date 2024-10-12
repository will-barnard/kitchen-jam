import axios from 'axios';

export default {

  updateSteps(steps) {
    return axios.post('/step' + steps)
  }

}