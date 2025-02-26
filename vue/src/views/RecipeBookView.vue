!<template>
    <div>
        <RecipeMenu />
        <LoadingWidget v-if="!showLoading"/>  
        <Transition name="quickFade">
            <div v-show="show">
                <RecipeBook :recipeList="$store.state.userRecipes"/>
            </div>
        </Transition>
        <div v-show="$store.state.loadedRecipes && $store.state.userRecipes.length < 1">
                <h3>You haven't logged any recipes yet</h3>
        </div>
    </div>
</template>

<script>
import RecipeMenu from '../components/RecipeMenu.vue';
import RecipeBook from '../components/RecipeBook.vue';
import LoadingWidget from '../components/LoadingWidget.vue';

export default {
    components: {RecipeMenu, RecipeBook, LoadingWidget},
    data() {
        return {
            recipeList: [],
            show: false
        }
    },
    created() {
        this.recipeList = this.$store.state.userRecipes;
        setTimeout(
        () => {this.show = true}
        , 10)
    },
    computed: {
        showLoading() {
            if (!this.$store.state.loadedRecipes) {
                return false;
            }
            else {
                return true;
            }
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