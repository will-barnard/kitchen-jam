<template>
    <div>
        <TopBanner />
        <Transition>
            <div v-if="loaded">
                <div class="category-name">
                    <h1>{{ category.categoryName }}</h1>
                </div>
                <div v-if="category.categoryType && category.categoryType.categoryTypeName" class="category-type">
                    <h3>{{ category.categoryType.categoryCategory }} - {{ category.categoryType.categoryTypeName }}</h3>
                </div>
                <i class="fas fa-edit edit-icon" @click="this.$router.push({ name: 'edit-categories' });"></i>
                <div class="single-row">
                    <h3 class="used-in">Used in:</h3>
                    <div class="spacer"></div>
                    <div class="toggle-buttons">
                        <h3 :class="{ active: showRecipes }" @click="showRecipes = true">Recipes</h3>
                        <h3 :class="{ active: !showRecipes }" @click="showRecipes = false">Meals</h3>
                    </div>
                </div>
                
                <RecipeCard v-if="showRecipes" v-for="recipe in recipeList" :key="recipe.recipeId" :recipe="recipe" />
                <MealCard v-else v-for="meal in mealList" :key="meal.mealId" :meal="meal" />
            </div>
        </Transition>
        <div v-if="notFound && loaded">
            <p>Category not found</p>
        </div>
    </div>
</template>

<script>
import RecipeCard from '../components/RecipeCard.vue';
import TopBanner from '../components/TopBanner.vue';
import MealCard from '../components/MealCard.vue';

export default {
    components: {TopBanner, RecipeCard, MealCard},
    data() {
        return {
            category: null,
            recipeList: [],
            mealList: [],
            notFound: false,
            loaded: false,
            showRecipes: true
        }
    },
    created() {
        if (this.$store.state.loadedCategories == false) {
            this.loadingTick();
        } else {
            this.category = this.$store.state.userCategories.find(category => category.categoryId == this.$route.params.categoryId);
            if (!this.category) {
                this.notFound = true;
            } else {
                this.getRecipes();
                this.getMeals();
            }
            this.loaded = true;
        }
    },
    methods: {
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedCategories == false) {
                    this.loadingTick();
                } else {
                    this.category = this.$store.state.userCategories.find(category => category.categoryId == this.$route.params.categoryId);
                    if (!this.category) {
                        this.notFound = true;
                    } else {
                        this.getRecipes();
                        this.getMeals();
                    }
                    this.loaded = true;
                }
            }, 500);
        },
        getRecipes() {
            this.recipeList = this.$store.state.userRecipes.filter(recipe => recipe.categoryId == this.category.categoryId);
        },
        getMeals() {
            this.mealList = this.$store.state.userMeals.filter(meal => {
                const recipe = this.$store.state.userRecipes.find(recipe => recipe.recipeId == meal.recipeId);
                return recipe && recipe.categoryId == this.category.categoryId;
            });
        },
        toggleList() {
            this.showRecipes = !this.showRecipes;
        }
    }
}
</script>

<style scoped>
    .category-name {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-bottom: 10px;
    }
    h1 {
        margin: 0;
        background-color: var(--light-8);
        border-radius: 10px;
        padding: 10px;
        padding-left: 20px;
        padding-right: 20px;
        margin: 5px;
    }
    .edit-icon {
        margin-left: 10px;
        font-size: 24px;
        cursor: pointer;
        color: var(--primary);
    }
    .used-in {
        margin-left: 10px;
    }
    .category-type {
        display: flex;
        justify-content: center;
        margin-bottom: 10px;
    }
    .spacer {
        flex-grow: 1;
    }
    i {
        margin-right: 10px;
    }
    .toggle-buttons {
        display: flex;
        justify-content: center;
        margin: 10px;
    }
    .toggle-buttons h3 {
        cursor: pointer;
        padding: 10px;
        margin: 0 5px;
        border: 2px solid var(--light-2);
        border-radius: 10px;
        background-color: var(--light-1);
    }
    .toggle-buttons .active {
        background-color: var(--light-5);
    }
</style>