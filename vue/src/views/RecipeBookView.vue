!<template>
    <div>
        <RecipeMenu />
        <RecipeBook />
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
    
</style>