import { createStore as _createStore } from 'vuex';
import axios from 'axios';

import MealService from '../services/MealService';
import RecipeService from '../services/RecipeService';
import ImageService from '../services/ImageService';
import ProfileService from '../services/ProfileService';
import TagService from '../services/TagService';
import CategoryService from '../services/CategoryService';
import FriendshipService from '../services/FriendshipService';
import NotificationService from '../services/NotificationService';


export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      userProfile: {},
      loadedProfile: false,
      userMeals: [],
      loadedMeals: false,
      userRecipes: [],
      loadedRecipes: false,
      userTags: [],
      loadedTags: false,
      userCategories: [],
      loadedCategories: false,
      subMenu: {},
      publicMealGallery: [],
      tagCategories: [],
      userFriends: [],
      loadedFriends: false,
      userPending: [],
      loadedPending: false,
      userRequests: [],
      loadedRequests: false,
      userNotifications: [],
      loadedNotifications: false,
      userOldNotifications: [],
      loadedOldNotifications: false
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
      INITIALIZE_USER() {
        this.commit('GET_USER_MEALS');
        this.commit('GET_USER_RECIPES');
        this.commit('GET_USER_PROFILE');
        this.commit('LOAD_USER_TAGS');
        this.commit('LOAD_USER_CATEGORIES');
        this.commit('LOAD_USER_FRIENDS');
        this.commit('LOAD_USER_NOTIFICATIONS');
      },
      GET_USER_PROFILE(state) {
        ProfileService.getPrincipalProfile().then(
          (response) => {
            state.userProfile = response.data;
            if (state.userProfile.imageId != null && state.userProfile.imageId > 0) {
              ImageService.getImage(state.userProfile.imageId).then(
                  (res) => {
                      const base64 = ImageService.parseImg(res);
                      state.userProfile.img = "data:image/png;base64," + base64;
                      state.loadedProfile = true;
                  }
              )
            } else {
              state.loadedProfile = true;
            }
          }
        )
      },
      UPDATE_PROFILE(state, payload) {
        state.userProfile = payload;
        if (state.userProfile.imageId != null && state.userProfile.imageId > 0) {
          ImageService.getImage(state.userProfile.imageId).then(
              (response) => {
                  const base64 = ImageService.parseImg(response);
                  state.userProfile.img = "data:image/png;base64," + base64;
              }
          )
      }
      },
      UPDATE_PROFILE_VISIBILITY(state, isPublic) {
        state.userProfile.public = isPublic;
      },
      UPDATE_POSTS_VISIBILITY(state, defaultPublic) {
        state.userProfile.defaultPublic = defaultPublic;
      },
      GET_USER_MEALS(state) {
        MealService.getMealsByUser().then(
          (response) => {
            state.userMeals = response.data.map((meal) => ({
              ...meal,
              img: null, // Initialize img as null for lazy loading
              ingredientList: meal.ingredientList || [],
            }));
            state.loadedMeals = true;

            // Public URLs are set immediately
            state.userMeals.forEach((meal) => {
              if (meal.public) {
                meal.publicUrl = "http://kitchen-jam.com/p/meal/" + meal.publicUrl;
              }
            });
          }
        );
      },
      GET_USER_RECIPES(state) {
        RecipeService.getRecipesByUser().then(
          (response) => {
            state.userRecipes = response.data
              .sort((a, b) => a.recipeName.localeCompare(b.recipeName))
              .map((recipe) => ({
                ...recipe,
                img: null, // Initialize img as null for lazy loading
                ingredientList: recipe.ingredientList || [],
              }));
            state.loadedRecipes = true;

            // Public URLs are set immediately
            state.userRecipes.forEach((recipe) => {
              if (recipe.public) {
                recipe.publicUrl = "http://kitchen-jam.com/p/recipe/" + recipe.publicUrl;
              }
            });
          }
        );
      },
      CREATE_MEAL(state, payload) {
        if (payload.ingredientList == null) {
          payload.ingredientList = [];
        }
        state.userMeals.unshift(payload);
      },
      UPDATE_MEAL(state, payload) {
        if (payload.ingredientList == null) {
          payload.ingredientList = [];
        }
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
        if (payload.ingredientList == null) {
          payload.ingredientList = [];
        }
        state.userRecipes.unshift(payload);
      },
      UPDATE_RECIPE(state, payload) {
        if (payload.ingredientList == null) {
          payload.ingredientList = [];
        }
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
      LOAD_USER_TAGS(state) {
        TagService.getTagCategories().then(
          (response) => {
            state.tagCategories = response.data;
          }
        )
        TagService.getTagsByUser().then(
          (response) => {
            state.userTags = response.data;
            state.loadedTags = true;
            const waitForMealsToLoad = () => {
              if (state.loadedMeals) {
                for (let meal of state.userMeals) {
                  if (meal.tags) {
                    for (let mealTag of meal.tags) {
                      let tag = state.userTags.find(tag => tag.tagId === mealTag.tagId);
                      if (tag) {
                        tag.timesUsed = (tag.timesUsed || 0) + 1;
                        if (tag.lastUsed) {
                          if (new Date(meal.dateCooked) > new Date(tag.lastUsed) || !tag.lastUsed) {
                            tag.lastUsed = meal.dateCooked;
                            tag.lastUsedMeal = meal.mealId;
                          }
                        } else {
                          tag.lastUsed = meal.dateCooked;
                          tag.lastUsedMeal = meal.mealId;
                        }
                      }
                    }
                  }
                }
                for (let tag of state.userTags) {
                  if (!tag.timesUsed) {
                    tag.timesUsed = 0;
                  }
                }
              } else {
                setTimeout(waitForMealsToLoad, 100); // Check again after 100ms
              }
            };
            waitForMealsToLoad();
          }
        );
      },
      ADD_TAG(state, payload) {
        state.userTags.push(payload);
        state.userTags.sort((a, b) => a.tagName.localeCompare(b.tagName));
      },
      EDIT_TAG(state, payload) {
        let i = state.userTags.findIndex(
          (tag) => {
            return tag.tagId == payload.tagId;
          }
        );
        state.userTags[i] = payload;
      },
      DELETE_TAG(state, payload) {
        TagService.deleteTag(payload).then(
          () => {
            state.userTags = state.userTags.filter(
              (tag) => {
                return tag.tagId != payload;
              }
            )
          }
        )
      },
      LOAD_USER_CATEGORIES(state) {
        CategoryService.getCategoriesByUser().then(
          (response) => {
            state.userCategories = response.data;
            state.loadedCategories = true;

            const waitForDataToLoad = () => {
              if (state.loadedRecipes && state.loadedMeals) {
                for (let category of state.userCategories) {
                  category.countRecipes = 0;
                  category.avgRating = 0;
                  category.lastCreated = null;
                  category.countMeals = 0;
                  let totalRating = 0;
                  let ratingCount = 0;

                  for (let recipe of state.userRecipes) {
                    if (recipe.categoryId === category.categoryId) {
                      category.countRecipes++;
                      if (recipe.avgRating) {
                        totalRating += recipe.avgRating;
                        ratingCount++;
                      }
                      if (!category.lastCreated || new Date(recipe.lastCreated) > new Date(category.lastCreated)) {
                        category.lastCreated = recipe.lastCreated;
                        const lastCreatedMeal = state.userMeals
                          .filter(meal => meal.recipeId === recipe.recipeId)
                          .sort((a, b) => new Date(b.dateCooked) - new Date(a.dateCooked))[0];
                        if (lastCreatedMeal) {
                          category.lastCreatedMeal = lastCreatedMeal.mealId;
                        }
                      }
                      for (let meal of state.userMeals) {
                        if (meal.recipeId === recipe.recipeId) {
                          category.countMeals++;
                        }
                      }
                    }
                  }

                  if (ratingCount > 0) {
                    category.avgRating = totalRating / ratingCount;
                  }
                }
              } else {
                setTimeout(waitForDataToLoad, 100); // Check again after 100ms
              }
            };
            waitForDataToLoad();
          }
        )
      },
      ADD_CATEGORY(state, payload) {
        state.userCategories.unshift(payload);
      },
      EDIT_CATEGORY(state, payload) {
        let i = state.userCategories.findIndex(
          (category) => {
            return category.categoryId == payload.categoryId;
          }
        );
        state.userCategories[i] = payload;
      },
      DELETE_CATEGORY(state, payload) {
        CategoryService.deleteCategory(payload).then(
          () => {
            state.userCategories = state.userCategories.filter(
              (category) => {
                return category.categoryId != payload;
              }
            )
          }
        )
      },
      LOAD_USER_FRIENDS(state) {
        FriendshipService.getFriendList().then(
          (response) => {
            state.userFriends = response.data;
            state.loadedFriends = true;
          }
        )
        FriendshipService.getFriendRequests().then(
          (response) => {
            state.userRequests = response.data;
            state.loadedRequests = true;
          }
        )
        FriendshipService.getPendingRequests().then(
          (response) => {
            state.userPending = response.data;
            state.loadedPending = true;
          }
        )
      },
      ADD_FRIEND(state, user) {
        state.userFriends.push(user);
        state.userPending = state.userPending.filter(request => request.friendId !== user.userId);
      },
      ADD_FRIEND_TO_PENDING(state, user) {
        state.userPending.push({ friendId: user.userId });
      },
      CANCEL_FRIEND_REQUEST(state, userId) {
        state.userPending = state.userPending.filter(request => request.friendId !== userId);
      },
      REJECT_FRIEND_REQUEST(state, payload) {
        state.userRequests = state.userRequests.filter(request => request.friendId !== payload.friendId);
      },
      DELETE_FRIEND(state, payload) {
        FriendshipService.removeFriend(payload).then(
          () => {
            state.userFriends = state.userFriends.filter(
              (friend) => {
                return friend.friendId != payload;
              }
            )
          }
        )
      },
      LOAD_USER_NOTIFICATIONS(state) {
        NotificationService.getNotifications().then(
          (response) => {
            state.userNotifications = response.data;

            const waitForPendingToLoad = () => {
              if (state.loadedRequests) {
                if (state.userRequests.length > 0) {
                  let notification = {
                    notificationId: -1,
                    type: 'friends',
                    message: state.userRequests.length == 1 ? 'You have a new friend request.' : 'You have ' + state.userRequests.length + ' new friend requests.',
                    createdAt: null,
                    read: false,
                  };
                  state.userNotifications.unshift(notification);
                }
                state.loadedNotifications = true;
              } else {
                setTimeout(waitForPendingToLoad, 100); // Check again after 100ms
              }
            };

            waitForPendingToLoad();
          }
        );
      },
      MARK_NOTIFICATION_AS_READ(state, notificationId) {
        NotificationService.markNotificationAsRead(notificationId).then(
          () => {
            const notification = state.userNotifications.find(notification => notification.notificationId === notificationId);
            if (notification) {
              notification.read = true;
              state.userOldNotifications.push(notification);
              state.userNotifications = state.userNotifications.filter(n => n.notificationId !== notificationId);
            }
         }
        )
      },
      LOAD_OLD_NOTIFICATIONS(state) {
        NotificationService.getOldNotifications().then(
          (response) => {
            state.userOldNotifications = response.data;
            state.loadedOldNotifications = true;
          }
        )
      },
      DELETE_NOTIFICATION(state, notificationId) {
        NotificationService.deleteNotification(notificationId).then(
          () => {
            state.userOldNotifications = state.userOldNotifications.filter(notification => notification.notificationId !== notificationId);
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
      },
      SAVE_IMAGE(state, { id, base64, type }) {
        if (type === 'meal') {
          const meal = state.userMeals.find(meal => meal.imageId === id);
          if (meal) meal.img = `data:image/png;base64,${base64}`;
        } else if (type === 'recipe') {
          const recipe = state.userRecipes.find(recipe => recipe.imageId === id);
          if (recipe) recipe.img = `data:image/png;base64,${base64}`;
        }
      }
    }
  });
  return store;
}
