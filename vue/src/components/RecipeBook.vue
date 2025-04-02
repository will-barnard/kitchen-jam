<template>
    <Transition>
       <div>
            <div class="search-area">
                <input type="text" v-model="searchTerm" placeholder="Search recipes..." class="search-input" />
            </div>
            <p v-if="!filteredRecipes.length">you have not logged any recipes</p>
            <TransitionGroup>
                <RecipeCard class="recipe-card" v-for="recipe in filteredRecipes" :key="recipe.recipeId" :recipe="recipe" :selected="select == recipe.recipeId" v-on:click="selectCard()"/>
            </TransitionGroup>
        </div>  
    </Transition>
</template>

<script>
import RecipeCard from '../components/RecipeCard.vue';
import LoadingWidget from './LoadingWidget.vue';

export default {
    components: {RecipeCard, LoadingWidget},
    props: ['recipeList'],
    data() {
        return {
            select: 0,
            searchTerm: ''
        }
    },
    computed: {
        filteredRecipes() {
            return this.recipeList.filter(recipe => 
                recipe.recipeName.toLowerCase().includes(this.searchTerm.toLowerCase())
            );
        }
    },
    methods: {
        selectCard() {

        }
    }
}
</script>

<style scoped>
    .search-input {
        padding: 0.5rem;
        width: 100%;
        box-sizing: border-box;
        border-radius: 5px;
        border: 1px var(--dark-1);
    }
    .search-area {
        display: flex;
        justify-content: center;
        margin-bottom: 1rem;
        padding: 1rem;
        background-color: var(--light-2);
        border-radius: 5px;
    }
    .v-enter-active,
    .v-leave-active {
        transition: opacity 0.5s ease;
    }
    .v-enter-from,
    .v-leave-to {
        opacity: 0;
        transition: opacity 0.5s ease;
    }
</style>