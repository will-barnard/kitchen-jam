<template>
    <Transition>
       <div>
            <p v-if="!recipeList">you have not logged any recipes</p>  
            <TransitionGroup>
                <RecipeCard class="recipe-card" v-if="recipeList" v-for="recipe in $store.state.userRecipes" :key="recipe.recipeId" :recipe="recipe" :selected="select == recipe.recipeId" v-on:click="selectCard()"/>
            </TransitionGroup>
        </div>  
    </Transition>
   
</template>

<script>
import RecipeService from '../services/RecipeService.js';
import RecipeCard from '../components/RecipeCard.vue';

export default {
    components: {RecipeCard},
    data() {
        return {
            recipeList: [],
            select: 0
        }
    },
    created() {
        if (!this.$store.state.userRecipes) {
            this.recipeList = this.$store.state.userRecipes;
        } else {
            RecipeService.getRecipesByUser().then(
                (response) => {
                    this.recipeList = response.data;
                    this.$store.commit('GET_USER_RECIPES', this.recipeList);
                }
            )
        }
    },
    methods: {
        selectCard() {

        }
    }
}
</script>

<style>
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