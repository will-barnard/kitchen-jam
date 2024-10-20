!<template>
    <div>
        <RecipeMenu />
        <Transition>
            <RecipeBook :recipeList="$store.state.userRecipes"/>
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
            recipeList: []
        }
    },
    created() {
        this.tab = 'log';
        this.recipeList = this.$store.state.userRecipes;
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