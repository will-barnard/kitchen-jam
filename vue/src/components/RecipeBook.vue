<template>
    <Transition>
       <div>
            <p v-if="!recipeList">you have not logged any recipes</p>
            <TransitionGroup>
                <RecipeCard class="recipe-card" v-if="recipeList" v-for="recipe in recipeList" :key="recipe.recipeId" :recipe="recipe" :selected="select == recipe.recipeId" v-on:click="selectCard()"/>
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
            select: 0
        }
    },
    created() {
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