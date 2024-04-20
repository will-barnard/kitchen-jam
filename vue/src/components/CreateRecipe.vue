<template>
    <div>

        <form v-on:submit.prevent="createRecipe()">
            <div>
                <label>Name</label><input type="text" v-model="recipe.recipeName">
                <label>Description</label><input type="text" v-model="recipe.description"/>
            </div>

            <div>
                <!-- img goes here -->
            </div>

            <input type="submit" value="Submit" />
        </form> 
        
    </div>
</template>

<script>
import RecipeService from '../services/RecipeService.js'

export default {
    data() {
        return {
            recipe: {}
        }
    },
    methods: {
        createRecipe() {
            RecipeService.createRecipe(this.recipe).then(
                () => {
                    this.$router.push({ name: 'cookbook' });
                }
            )
        }
    }
}
</script>

<style>
    
</style>