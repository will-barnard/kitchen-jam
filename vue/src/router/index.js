import { createRouter as createRouter, createWebHistory } from 'vue-router'
import { useStore } from 'vuex'

import HomeView from '../views/HomeView.vue';
import LoginView from '../views/LoginView.vue';
import LogoutView from '../views/LogoutView.vue';
import RegisterView from '../views/RegisterView.vue';
import MealView from '../views/MealView.vue';
import RecipeBookView from '../views/RecipeBookView.vue';
import CreateMealView from '../views/CreateMealView.vue';
import MealDetailView from '../views/MealDetailView.vue';
import CreateRecipeView from '../views/CreateRecipeView.vue';
import RecipeDetailView from '../views/RecipeDetailView.vue'
import AboutView from '../views/AboutView.vue';
import WillView from '../views/WillView.vue';
import GalleryMealDetailView from '../views/GalleryMealDetailView.vue';
import WalkthroughView from '../views/WalkthroughView.vue';
import PublicMealView from '../views/PublicMealView.vue';
import PublicRecipeView from '../views/PublicRecipeView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/logout",
    name: "logout",
    component: LogoutView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/log",
    name: "meal-log",
    component: MealView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/log/new",
    name: "new-meal",
    component: CreateMealView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/log/:mealId",
    name: "meal-detail",
    component: MealDetailView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/meal/public/:mealId",
    name: "public-meal-detail",
    component: GalleryMealDetailView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/cookbook",
    name: "cookbook",
    component: RecipeBookView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/cookbook/new",
    name: "new-recipe",
    component: CreateRecipeView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/cookbook/:recipeId",
    name: "recipe-detail",
    component: RecipeDetailView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
  },
  {
    path: '/will',
    name: 'will',
    component: WillView
  },
  {
    path: '/walkthrough',
    name: 'walkthrough',
    component: WalkthroughView
  },
  {
    path: '/p/meal/:uuid',
    name: 'public-meal',
    component: PublicMealView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/p/recipe/:uuid',
    name: 'public-recipe',
    component: PublicRecipeView,
    meta: {
      requiresAuth: false
    }
  },
];

// Create the router
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition;
    } else {
      window.scrollTo(0, 0);
    }
  }
});

router.beforeEach((to) => {

  // Get the Vuex store
  const store = useStore();

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return {name: "login"};
  }
  // Otherwise, do nothing and they'll go to their next destination

});

export default router;
