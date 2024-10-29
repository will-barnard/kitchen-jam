!<template>
    <div>
        <RecipeMenu />
        <Transition name="quickFade">
            <div v-show="show">
                <RecipeBook :recipeList="$store.state.userRecipes"/>
            </div>
        </Transition>
    </div>
</template>

<script>
import RecipeMenu from '../components/RecipeMenu.vue';
import RecipeBook from '../components/RecipeBook.vue';

export default {
    components: {RecipeMenu, RecipeBook},
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