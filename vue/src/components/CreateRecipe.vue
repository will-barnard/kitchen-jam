<template>
    <div>
        <h1>{{ getName }}</h1>
        <form v-on:submit.prevent="createRecipe()">
            <div class="container">
                    <label>Name</label><input type="text" v-model="recipe.recipeName">
                    <label>Description</label><input type="text" v-model="recipe.description"/>
            </div>

            <div>
                <!-- img goes here -->
            </div>

            <input type="submit" value="Create Recipe" class="submit"/>
        </form> 
        
    </div>
</template>

<script>
import RecipeService from '../services/RecipeService.js'

export default {
    data() {
        return {
            recipe: {},
            name: ""
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
    },
    computed: {
        getName() {
            if (!this.recipe.recipeName) {
                return "New Recipe"
            } else {
                return this.recipe.recipeName
            }
        }
    }
}
</script>

<style scoped>
    .container {
        border: 1px solid;
        border-radius: 10px;
        padding: 10px;
        display: flex;
        flex-direction: column;
        margin-bottom: 10px;
        background-color: var(--light-2);
    }
    .container input, label {
        margin: 5px;
    }
    .submit {
        width: 100%;
    }
</style>