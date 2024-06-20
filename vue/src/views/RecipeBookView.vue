!<template>
    <div>
        <RecipeMenu />
        <Transition>
            <RecipeBook />
        </Transition>
    </div>
</template>

<script>
import RecipeService from '../services/RecipeService.js';
import RecipeMenu from '../components/RecipeMenu.vue';
import RecipeBook from '../components/RecipeBook.vue';

export default {
    components: {RecipeMenu, RecipeBook}
    
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