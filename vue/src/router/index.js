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
import WalkthroughView from '../views/WalkthroughView.vue';
import PublicMealView from '../views/PublicMealView.vue';
import PublicRecipeView from '../views/PublicRecipeView.vue';
import UpdatesView from '../views/UpdatesView.vue';
import ProfileView from '../views/ProfileView.vue';
import DashboardView from '../views/DashboardView.vue';
import FriendsView from '../views/FriendsView.vue';
import SettingsView from '../views/SettingsView.vue';
import NotificationsView from '../views/NotificationsView.vue';
import RequestPasswordResetView from '../views/RequestPasswordResetView.vue';
import PasswordResetView from '../views/PasswordResetView.vue';
import EditTagsView from '../views/EditTagsView.vue';
import EditCategoriesView from '../views/EditCategoriesView.vue';
import JamView from '../views/JamView.vue';
import TagView from '../views/TagView.vue';
import CategoryView from '../views/CategoryView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      requiresAuth: false,
      title: 'Home - Kitchen Jam'
    }
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: {
      requiresAuth: false,
      title: 'Login - Kitchen Jam'
    }
  },
  {
    path: "/logout",
    name: "logout",
    component: LogoutView,
    meta: {
      requiresAuth: true,
      title: 'Logout - Kitchen Jam'
    }
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: {
      requiresAuth: false,
      title: 'Register - Kitchen Jam'
    }
  },
  {
    path: "/log",
    name: "meal-log",
    component: MealView,
    meta: {
      requiresAuth: true,
      title: 'Meal Log - Kitchen Jam'
    }
  },
  {
    path: "/log/new",
    name: "new-meal",
    component: CreateMealView,
    meta: {
      requiresAuth: true,
      title: 'Create Meal - Kitchen Jam'
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
    path: "/cookbook",
    name: "cookbook",
    component: RecipeBookView,
    meta: {
      requiresAuth: true,
      title: 'Cookbook - Kitchen Jam'
    }
  },
  {
    path: "/cookbook/new",
    name: "new-recipe",
    component: CreateRecipeView,
    meta: {
      requiresAuth: true,
      title: 'Create Recipe - Kitchen Jam'
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
    meta: {
      title: 'About - Kitchen Jam'
    }
  },
  {
    path: '/walkthrough',
    name: 'walkthrough',
    component: WalkthroughView,
    meta: {
      title: 'Walkthrough - Kitchen Jam'
    }
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
  {
    path: '/updates',
    name: 'updates',
    component: UpdatesView,
    meta: {
      requiresAuth: false,
      title: 'Site Updates - Kitchen Jam'
    }
  },
  {
    path: '/profile/:userId',
    name: 'profile',
    component: ProfileView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: DashboardView,
    meta: {
      requiresAuth: true,
      title: 'Dashboard - Kitchen Jam'
    }
  },
  {
    path: '/friends',
    name: 'friends',
    component: FriendsView,
    meta: {
      requiresAuth: true,
      title: 'Friends - Kitchen Jam'
    }
  },
  {
    path: '/settings',
    name: 'settings',
    component: SettingsView,
    meta: {
      requiresAuth: true,
      title: 'Settings - Kitchen Jam'
    }
  },
  {
    path: '/notifications',
    name: 'notifications',
    component: NotificationsView,
    meta: {
      requiresAuth: true,
      title: 'Notifications - Kitchen Jam'
    }
  },
  {
    path: "/passwordResetRequest",
    name: "password-reset-request",
    component: RequestPasswordResetView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: "/resetPassword/:uuid",
    name: "password-reset",
    component: PasswordResetView,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/tags/edit',
    name: 'edit-tags',
    component: EditTagsView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/categories/edit',
    name: 'edit-categories',
    component: EditCategoriesView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/jam',
    name: 'jam',
    component: JamView,
    meta: {
      requiresAuth: true,
      title: 'Jam Page - Kitchen Jam'
    }
  },
  {
    path: '/tag/:tagId',
    name: 'tag-detail',
    component: TagView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/category/:categoryId',
    name: 'category-detail',
    component: CategoryView,
    meta: {
      requiresAuth: true
    }
  }
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

  // Set the document title dynamically
  if (to.meta.title) {
    document.title = to.meta.title;
  }

  // Determine if the route requires Authentication
  const requiresAuth = to.matched.some(x => x.meta.requiresAuth);

  // If it does and they are not logged in, send the user to "/login"
  if (requiresAuth && store.state.token === '') {
    return { name: "login" };
  }
  // Otherwise, do nothing and they'll go to their next destination
});

export default router;
