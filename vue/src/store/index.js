import { createStore as _createStore } from 'vuex';
import axios from 'axios';

import MealService from '../services/MealService';
import RecipeService from '../services/RecipeService';
import ImageService from '../services/ImageService';


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
      GET_USER_MEALS(state) {
        MealService.getMealsByUser().then(
          (response) => {
            state.userMeals = response.data;
            for (let meal of state.userMeals) {
              ImageService.getImage(meal.imageId).then(
                (res) => {
                  const base64 = ImageService.parseImg(res);
                  meal.img = "data:image/png;base64," + base64;
                }
              )
            }
          }
        )
      },
      GET_USER_RECIPES(state) {
        RecipeService.getRecipesByUser().then(
          (response) => {
            state.userRecipes = response.data;
            for (let recipe of state.userRecipes) {
              ImageService.getImage(recipe.imageId).then(
                (res) => {
                  const base64 = ImageService.parseImg(res);
                  recipe.img = "data:image/png;base64," + base64;
                }
              )
            }
          }
        )
      },
      CREATE_MEAL(state, payload) {
        MealService.createMeal(payload).then(
          (response) => {
            state.userMeals.push(response.data);
          }
        )
      },
      UPDATE_MEAL(state, payload) {
        MealService.updateMeal(payload).then(
          (response) => {
            state.userMeals.filter(
              (meal) => {
                return meal.mealId != response.data.mealId;
              }
            ).push(response.data);
          }
        )
      },
      DELETE_MEAL(state, payload) {
        MealService.deleteMeal(payload).then(
          () => {
            state.userMeals.filter(
              (meal) => {
                return meal.mealId != payload;
              }
            )
          }
        )
      },
      CREATE_RECIPE(state, payload) {
        RecipeService.createRecipe(payload).then(
          (response) => {
            state.userRecipes.push(response.data);
          }
        )
      },
      UPDATE_RECIPE(state, payload) {
        RecipeService.updateRecipe(payload).then(
          (response) => {
            state.userRecipes.filter(
              (recipe) => {
                return recipe.recipeId != response.data.recipeId;
              }
            ).push(response.data);
          }
        )
      },
      DELETE_RECIPE(state, payload) {
        RecipeService.deleteRecipe(payload).then(
          () => {
            state.userRecipes.filter(
              (recipe) => {
                return recipe.recipeId != payload;
              }
            )
          }
        )
      }
    },
  });
  return store;
}
