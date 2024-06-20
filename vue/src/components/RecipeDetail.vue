<template>
    <body>
       
        <div class="recipe-img">
                   <img src="../img/placeholder.jpeg" />
                   <!-- img goes here -->
        </div>

       <div v-show="!editing">
           <div>
               <div class="details">
                   <div class="title">
                       <h2 >{{ staticRecipe.recipeName }}</h2>
                   </div>
                   <div class="subtitle">
                    <p>{{ staticRecipe.description }}</p>   
                   </div>
                   <!-- <h3>Last made here?</h3> -->

                   <!-- <div class="category">
                   </div> -->

                    <div class="widgets">
                        <div class="widgets">
                            <div class="cooktime">
                                <img src="/img/clock.png" />
                                <p>{{ staticRecipe.avgCookTime }} min</p>
                            </div>
                        </div>
                        <div class="rating">
                           <p>rating / 5 Rating</p>
                       </div>
                   </div>

                   
               </div>            
           </div>
       </div>

       <div v-show="editing">
           <form>
               <div class="edit-form">
                    <h3>Edit details</h3>

                   <div class="input-area">
                       <label>Name</label>
                       <input type="text" v-model="newRecipe.recipeName">
                   </div>
                   <div class="input-area">
                       <label>Description</label>
                       <textarea v-model="newRecipe.description"></textarea>
                   </div>
            
               </div>
           </form>
           <div class="edit-form">
                <h3>Edit Category</h3>
                <div>
                    <div v-if="newRecipe.categoryId" class="current-category">
                        <img src="/img/minus.png" class="minus mini-button" @click="removeCategory()">
                        <div class="tag-spacer"></div>
                        <h4>{{ newRecipe.categoryName }}</h4>
                    </div>
                    <div class="category-search">
                        <input type="text" v-model="newCategory.recipeName" @keyup="searchForCategory()"/>
                        <button @click="createRecipe()">Create New Category</button>
                    </div>
                    <div class="category-search-results" v-if="searchCategory">
                        <div v-for="category in searchCategory" class="recipe">
                            <img src="/img/plus.png" class="plus mini-button" @click="addCategory(recipe)"/>
                            <div class="tag-spacer"></div>
                            <p>{{ recipe.recipeName }}</p>
                        </div>
                    </div>
                </div>
           </div>
       </div>



       <div class="delete" v-if="deleting">
           <p id="delete-check">Are you sure you want to delete? This cannot be undone</p>
           <h2 class="yes-delete" v-on:click="deleteRecipe()">Delete</h2>
           <h2 class="cancel-delete" v-on:click="deleting = false">Cancel</h2>
       </div>
       <div class="controls" v-show="!deleting">
           <div class="edit-button button" v-if="!editing" v-on:click="editing=true">
                <img src="/img/edit.png" />
            </div>
            <div class="trash button" v-if="!editing" v-on:click="deleteButton()">
                <img src="/img/trash.png" />
            </div>
            <div class="undo button" v-if="editing" v-on:click="cancelEdit()">
                <img src="/img/undo.png" />
            </div>
            <div class="check button" v-if="editing" v-on:click="saveEdit()">
                <img src="/img/check.png" />
            </div>
       </div>       
       <div class="meals" v-if="recipe.mealList && !editing">
            <h2>Meals</h2>
            <MealCard v-for="meal in recipe.mealList" :key="meal.mealId" :meal="meal"></MealCard>
       </div>
   </body>
</template>

<script>
import RecipeService from '../services/RecipeService.js'
import Tag from './Tag.vue';
import MealCard from '../components/MealCard.vue';

export default {
    props: ['recipe', 'loading'],
    components: {Tag, MealCard},
    data() {
        return {
            editing: false,
            newRecipe: {},
            staticRecipe: {},
            deleting: false,
            newTag: {},
            newCategory: {},
            searchCategory: []
        }
    },
    created() {
        this.staticRecipe = this.cloneRecipe(this.recipe);
        this.newRecipe = this.cloneRecipe(this.staticRecipe);
    },
    methods: {
        cancelEdit() {
            this.editing = false;
            this.newRecipe = this.cloneRecipe(this.staticRecipe);
        },
        saveEdit() {
            RecipeService.updateRecipe(this.newRecipe).then(
                (response) => {
                    this.staticRecipe = response.data;
                    this.newRecipe = this.staticRecipe;
                    this.editing = false;
                }
            )
        },
        deleteButton() {
            if (!this.deleting) {
                this.deleting = true;
            }
        },
        deleteRecipe() {
            RecipeService.deleteRecipe(this.recipe.recipeId).then(
                () => {
                    this.$router.push({name: 'cookbook'})
                }
            )
        },
        cloneRecipe(recipe) {
            let newRecipe = {};
            if (recipe) {
                newRecipe.recipeId = recipe.recipeId;
                newRecipe.userId = recipe.userId;
                newRecipe.recipeName = recipe.recipeName;
                newRecipe.description = recipe.description;
                newRecipe.avgCookTime = recipe.avgCookTime;
                newRecipe.imageId = recipe.imageId;
                newRecipe.isPublic = recipe.isPublic;
                newRecipe.categoryId = recipe.categoryId;
                newRecipe.mealList = recipe.mealList;

                newRecipe.categories = recipe.categories;

            }
            return newRecipe;
        }
    }
}
</script>

<style scoped>
    body p, h1, h2, h3, h4 {
        margin: 0px;
    }
    .recipe-img {
        margin-top: 15px;
        overflow: hidden;
        text-align: center;
        margin-bottom: 15px;
        margin-left: 5px;
        margin-right: 5px;
    }
    .recipe-img img {
        width: 100%;
        border-radius: 15px;
    }
    .controls {
        text-align: center;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 0px;
    }
    .controls:hover {
        cursor: pointer;
    }
    .controls h2 {
        border: 1px solid black;
        padding: 5px;
        margin: 0px;
    }
    .delete {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    .delete h2 {
        border: 1px solid black;
        margin: 5px;
        padding: 5px;
    }
    .delete {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 10px;
    }
    .delete h2 {
        margin: 5px;
        padding: 5px;
        border-radius: 10px;
    }
    .yes-delete {
        background-color: var(--light-3);
    }
    .cancel-delete {
        background-color: var(--edit);
    }
    .delete:hover {
        cursor: pointer;
    }
    #delete-check:hover {
        cursor: default;
    }
    .spacer {
        flex-grow: 1;
    }
    .controls {
        text-align: center;
        display: flex;
        flex-direction: row;
        justify-content: end;
        align-items: center;
        margin-right: 5px;
        margin-top: 10px;
    }
    .controls img {
        height: 5vh;
    }
    .controls:hover {
        cursor: pointer;
    }
    .button {
        width: 15vw;
        /* border: 1px solid var(--border-color); */
        border-radius: 10px;
        padding: 5px;
        margin-right: 5px;
    }
    .edit-button {
        background-color: var(--edit);
    }
    .trash {
        background-color: var(--light-3);
    }
    .check {
        background-color: var(--light-4);
    }
    .undo {
        background-color: var(--edit);
    }
    .details {
        background-color: var(--light-2);
        padding: 15px;
        margin: 5px;
        border-radius: 10px;
    }
    .title {
        display: flex;
        flex-direction: column;
    }
    .title h2{
        text-align: center;
        background-color: var(--light-5);
        border-radius: 10px;
        padding: 10px;
        margin: 0px;
        margin-bottom: 5px;
    }
    .title h3 {
        text-align: center;
        background-color: var(--light-5);
        border-radius: 10px;
        padding: 10px;
        margin: 0px;
        margin-top: 5px;

    }
    .title h4{
        margin-top: 5px;
        margin-bottom: 0px;
    }
    .subtitle {
        margin-left: 0px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        flex-grow: 1;
    }
    .widgets {
        display: flex;
        margin-top: 15px;
        justify-content: center;
        align-items: center;
        margin-left: 5px;
        margin-right: 5px;
        margin-bottom: 15px;
    }
    .cooktime {
        width: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .cooktime img {
        height: 80px;
        margin-right: 5px;
    }
    .rating {
        width: 50%;
        text-align: center;
    }
    .meals h2 {
        margin: 10px;
    }
    .input-area {
        display: flex;
        flex-direction: column;
        margin-bottom: 5px;
    }
    .edit-form {
        display: flex;
        flex-direction: column;
        background-color: var(--light-2);
        margin: 5px;
        padding: 10px;
        border-radius: 10px;
    }
    .edit-form h3 {
        margin-bottom: 5px;
    }
    .category-search {
        display: flex;
        margin-bottom: 5px;
    }
    .category-search input {
        margin: 0px;
        flex-grow: 1;
    }
</style>