import { createStore as _createStore } from 'vuex';
import axios from 'axios';


export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      userMeals: [],
      userRecipes: [],
      subMenu: {}
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        axios.defaults.headers.common = {};
      },
      GET_USER_MEALS(state, payload) {
        state.userMeals = payload;
      },
      GET_USER_RECIPES(state, payload) {
        state.userRecipes = payload;
      }
    },
  });
  return store;
}
