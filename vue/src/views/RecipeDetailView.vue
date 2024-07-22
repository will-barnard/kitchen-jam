<template>
    <div>
        <RecipeMenu />
        <p v-if="loading  && showLoading">Loading...</p>
        <Transition>
            <RecipeDetail v-if="!loading" :recipe="getRecipe"/>
        </Transition>
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
            loading: true,
            showLoading: false
        }
    },
    created() {
        this.loadingWait();
        if (this.$store.state.userRecipes.find((recipe) => {return recipe.recipeId == this.$route.params.recipeId;})) {
            this.getRecipe = this.$store.state.userRecipes.find(
                (recipe) => {
                    return recipe.recipeId == this.$route.params.recipeId;
                }
            );
            this.loading = false;
        } else {
            RecipeService.getRecipe(this.$route.params.recipeId).then(
                (response) => {
                    this.getRecipe = response.data;
                    this.loading = false;
                }
            )
        }
    },
    methods: {
        loadingWait() {
            setTimeout( () => {
                this.showLoading = true;
            }, 5000)
        }
    }
}
</script>

<style scoped>
    .v-enter-active,
    .v-leave-active {
        transition: opacity .5s ease;
    }

    .v-enter-from,
    .v-leave-to {
        opacity: 0%;
    }
</style>