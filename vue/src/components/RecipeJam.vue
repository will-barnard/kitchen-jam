<template>
    <div>
        <div class="block" v-show="!compact || compactShow == 'oldRecipes'">
            <h3>Recipes you have not made recently:</h3>
            <MiniRecipeCard v-for="recipe in oldRecipes" :key="recipe.recipeId" :recipe="recipe"/>
        </div>
        <div class="block " v-show="!compact || compactShow == 'improveRecipes'">
            <h3>Recipes with room for improvement:</h3>
            <MiniRecipeCard v-for="recipe in improveRecipes" :key="recipe.recipeId" :recipe="recipe"/>
        </div>
    </div>
</template>
<script>
import MiniRecipeCard from './MiniRecipeCard.vue';

export default {
    props: ['compact'],
    components: {MiniRecipeCard},
    data() {
        return {
            compactShow: ""
        }
    },
    computed: {
        oldRecipes() {
            let sortedRecipes = this.$store.state.userRecipes.slice().sort((a, b) => new Date(a.lastCreated) - new Date(b.lastCreated));
            let firstTenRecipes = sortedRecipes.slice(0, 10);
            let shuffledRecipes = firstTenRecipes.sort(() => 0.5 - Math.random());
            return shuffledRecipes.slice(0, 3);
        },
        improveRecipes() {
            let sortedRecipes = this.$store.state.userRecipes.slice().sort((a, b) => a.avgRating - b.avgRating);
            let topRecipes = sortedRecipes.slice(0, 10);
            let shuffledRecipes = topRecipes.sort(() => 0.5 - Math.random());
            return shuffledRecipes.slice(0, 3);
        }
    },
    created() {
        if (this.compact) {
            let randomInt = Math.floor(Math.random() * 2);
            if (randomInt == 0) {
                this.compactShow = "oldRecipes";
            } else {
                this.compactShow = "improveRecipes";
            }
        }
    }
}
</script>
<style>
    
</style>