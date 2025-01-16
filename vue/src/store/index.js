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
      loadedMeals: false,
      userRecipes: [],
      loadedRecipes: false,
      subMenu: {},
      publicMealGallery: []
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
            state.loadedMeals = true;
            for (let meal of state.userMeals) {
              if (meal.public) {
                meal.publicUrl = "http://kitchen-jam.com/p/meal/" + meal.publicUrl;
              }
              if (meal.imageId) {
                ImageService.getImage(meal.imageId).then(
                  (res) => {
                    const base64 = ImageService.parseImg(res);
                    meal.img = "data:image/png;base64," + base64;
                  }
                )
              }  
            }
          }
        )
      },
      GET_USER_RECIPES(state) {
        RecipeService.getRecipesByUser().then(
          (response) => {
            state.userRecipes = response.data;
            state.loadedRecipes = true;
            for (let recipe of state.userRecipes) {
              if (recipe.public) {
                recipe.publicUrl = "http://kitchen-jam.com/p/recipe/" + recipe.publicUrl;
              }
              if (recipe.imageId) {
                ImageService.getImage(recipe.imageId).then(
                  (res) => {
                    const base64 = ImageService.parseImg(res);
                    recipe.img = "data:image/png;base64," + base64;
                  }
                )
              } 
            }
          }
        )
      },
      CREATE_MEAL(state, payload) {
        state.userMeals.unshift(payload);
      },
      UPDATE_MEAL(state, payload) {
        let i = state.userMeals.findIndex(
          (meal) => {
            return meal.mealId == payload.mealId;
          }
        );
        state.userMeals[i] = payload;
      },
      DELETE_MEAL(state, payload) {
        MealService.deleteMeal(payload).then(
          () => {
            state.userMeals = state.userMeals.filter(
              (meal) => {
                return meal.mealId != payload;
              }
            )
          }
        )
      },
      CREATE_RECIPE(state, payload) {
        state.userRecipes.unshift(payload);
      },
      UPDATE_RECIPE(state, payload) {
        let i = state.userRecipes.findIndex(
          (recipe) => {
            return recipe.recipeId == payload.recipeId;
          }
        );
        state.userRecipes[i] = payload;
      },
      DELETE_RECIPE(state, payload) {
        RecipeService.deleteRecipe(payload).then(
          () => {
            state.userRecipes = state.userRecipes.filter(
              (recipe) => {
                return recipe.recipeId != payload;
              }
            )
          }
        )
      },
      LOAD_MEAL_GALLERY(state, payload) {
        state.publicMealGallery = payload;
        for (let meal of state.publicMealGallery) {
          if (meal.imageId) {
            ImageService.getImage(meal.imageId).then(
              (res) => {
                const base64 = ImageService.parseImg(res);
                meal.img = "data:image/png;base64," + base64;
              }
            )
          }
        }
      }
    }
  });
  return store;
}
