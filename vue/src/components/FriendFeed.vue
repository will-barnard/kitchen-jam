<template>
    <div>
        <hr />
        <div v-if="$store.state.loadedFriends && $store.state.userFriends.length === 0" class="no-friends">
            <p>No friends yet. Search for users to add as friends.</p>
        </div>
        <div v-if="loading">
            <LoadingWidget />
        </div>
        <div v-if="$store.state.loadedFriends && $store.state.userFriends.length > 0">
            
            <div class="toggle-buttons">
                <h3 :class="{ active: showEnum === 'meals' }" @click="showEnum = 'meals'">Meals</h3>
                <h3 :class="{ active: showEnum === 'recipes' }" @click="showEnum = 'recipes'">Recipes</h3>
            </div>
            <Transition name="fade">
                <div v-if="!loading && showEnum === 'meals'">
                    <div v-for="meal in meals" :key="meal.id">
                        <MealCard :meal="meal" :isFeed="true" />
                    </div>
                    <div class="button-container">
                        <button v-if="!loadingMore && !compact" @click="loadMoreMeals">Load More</button>
                        <p v-if="loadingMore">Loading more meals...</p>
                        <button v-if="compact" @click="$router.push({name: 'friends'})">Friend Page</button>
                    </div>
                </div>
            </Transition>
            <Transition name="fade">
                <div v-if="!loading && showEnum === 'recipes'">
                    <div v-for="recipe in recipes" :key="recipe.id">
                        <RecipeCard :recipe="recipe" :isFeed="true" />
                    </div>
                    <div class="button-container">
                        <button v-if="!loadingMore && !compact" @click="loadMoreRecipes">Load More</button>
                        <p v-if="loadingMore">Loading more recipes...</p>
                        <button v-if="compact" @click="$router.push({name: 'friends'})">Friend Page</button>
                    </div>
                </div>
            </Transition>
        </div>
    </div>
</template>

<script>
import FeedService from '../services/FeedService';
import MealCard from './MealCard.vue';
import LoadingWidget from './LoadingWidget.vue';
import RecipeCard from './RecipeCard.vue';

export default {
    props: ['compact'],
    components: {
        MealCard, LoadingWidget, RecipeCard
    },
    data() {
        return {
            showEnum: "meals",
            meals: [],
            recipes: [],
            loading: true,
            loadingMore: false,
            error: null,
            timesLoadedMeals: 0,
            timesLoadedRecipes: 0,
        }
    },
    methods: {
        loadMoreMeals() {
            this.timesLoadedMeals++
            this.loadingMore = true;
            FeedService.getMoreMeals(this.timesLoadedMeals).then(
                (response) => {
                    for (let meal of response.data) {
                        if (!meal.tags) {
                            meal.tags = [];
                        }
                    }
                    this.meals = [...this.meals, ...response.data];
                    this.loadingMore = false;
                }
            ).catch((error) => {
                this.error = error;
                this.loadingMore = false;
            });
        },
        loadMoreRecipes() {
            this.timesLoadedRecipes++;
            this.loadingMore = true;
            FeedService.getMoreRecipes(this.timesLoadedRecipes).then(
                (response) => {
                    this.recipes = [...this.recipes, ...response.data];
                    this.loadingMore = false;
                }
            ).catch((error) => {
                this.error = error;
                this.loadingMore = false;
            });
        }
    },
    created() {
        FeedService.getMealFeed().then( 
            (response) => {
                for (let meal of response.data) {
                    if (!meal.tags) {
                        meal.tags = [];
                    }
                }
                this.meals = this.compact ? response.data.slice(0, 3) : response.data;
                this.loading = false;
            }
        ).catch((error) => {
            this.error = error;
            this.loading = false;
        });
        FeedService.getRecipeFeed().then( 
            (response) => {
                this.recipes = this.compact ? response.data.slice(0, 3) : response.data;
                this.loading = false;
            }
        ).catch((error) => {
            this.error = error;
            this.loading = false;
        });
    }
}
</script>

<style scoped>
.toggle-buttons {
    display: flex;
    justify-content: center;
    margin: 10px;
}
.toggle-buttons h3 {
    cursor: pointer;
    padding: 10px;
    margin: 0 5px;
    border: 2px solid var(--light-1);
    border-radius: 10px;
    background-color: var(--light-1);
}
.toggle-buttons .active {
    background-color: var(--light-2);
}
.toggle-buttons h3 {
    font-size: 1em;
}
.button-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

button {
    padding: 10px 20px;
    font-size: 16px;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

button:hover {
    background-color: #0056b3;
}
.no-friends {
    text-align: center;
    margin-top: 20px;
}
hr {
    border: none;
    height: 2px;
    background-color: var(--border-color);
    border-radius: 5px;
    margin: 15px 15px 0px 15px;;
    opacity: 50%;
}
</style>