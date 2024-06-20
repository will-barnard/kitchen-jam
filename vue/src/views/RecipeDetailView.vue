<template>
    <div>
        <RecipeMenu />
        <RecipeDetail v-if="!loading" :recipe="getRecipe"/>
    </div>
</template>

<script>
import RecipeService from '../services/RecipeService.js'
import RecipeMenu from '../components/RecipeMenu.vue';
import RecipeDetail from '../components/RecipeDetail.vue';

export default {
    components: {RecipeMenu, RecipeDetail},
    data() {
        return {
            getRecipe: {},
            loading: true
        }
    },
    created() {
        RecipeService.getRecipe(this.$route.params.recipeId).then(
            (response) => {
                this.getRecipe = response.data;
                this.loading = false;
            }
        )
    }
}
</script>

<style>
    
</style>