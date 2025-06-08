<template>
    <body v-show="showForm">
        <h1>New Meal</h1>
        <div class="image-block">
            <div class="edit-block">
                <div class="image" v-if="showImage">
                    <img :src="imgPath" />
                </div>
                <div class="add-image">
                    <h3>Add image</h3>
                    <input type="file" name="file" accept="image/*" @change="uploadImage">
                </div>
            </div>
            
        </div>
        <div class="details">
            <form v-on:submit.prevent="createMeal()">
                <div class="form">
                    <div class="edit-block">
                        <h3>
                            Meal Details
                        </h3>
                        <label>Meal Name</label><input type="text" v-model="meal.mealName">
                        <label>Date cooked</label><input type="date" v-model="meal.dateCooked"/>
                        <label>Comment</label><input type="text" v-model="meal.mealComment"/>
                        <label>Cook time (min)</label><input type="number" v-model="meal.cookTime"/>
                        <label>Rating</label>
                        <div class="rating-container">
                            <div class="stars">
                                <i v-for="n in 5" :key="n" :class="{
                                    'fas fa-star': meal.rating >= n * 2,
                                    'fas fa-star-half-alt': meal.rating >= (n * 2) - 1 && meal.rating < n * 2,
                                    'far fa-star': meal.rating < (n * 2) - 1
                                }"></i>
                            </div>
                            <input type="range" min="0" max="10" step="1" v-model="meal.rating"/>
                        </div>
                        <label>Notes</label><textarea v-model="meal.notes"></textarea>
                    </div>
   
                    <div class="edit-recipe edit-block">
                        <h3>Recipe</h3>
                        <div v-if="meal.recipeId" class="current-recipe">
                            <i class="fas fa-minus minus mini-button" @click="removeRecipe()"></i>
                            <h4>{{ meal.recipeName }}</h4>
                        </div>
                        <div class="recipe-search">
                            <input type="text" v-model="newRecipe.recipeName" @keyup="searchForRecipes()"/>
                            <button @click.prevent="newRecipe.recipeName ? createRecipe() : null">Create New Recipe</button>
                        </div>
                        <div class="recipe-search-results">
                            <div v-for="recipe in searchRecipe" class="recipe">
                                <i class="fas fa-plus plus mini-button" @click="addRecipe(recipe)"></i>
                                <p>{{ recipe.recipeName }}</p>
                            </div>
                        </div>
                    </div>

                    <div class="edit-tags edit-block">
                        <h3>Tags</h3>
                        <div>
                            <div class="single-row">
                                <p v-if="meal.tags.length == 0">no tags</p>
                                <div class="spacer"></div>
                                <button v-if="meal.recipeId && (!meal.tags || meal.tags.length === 0)" @click="copyTagsFromLastTime">Copy tags from last time</button>
                            </div>
                            <div class="search-tags">
                                <div v-for="tag in meal.tags" class="tag-search-item">
                                    <p>{{tag.tagName}}</p>
                                    <i class="far fa-trash-alt delete-tag mini-button" @click="removeTag(tag.tagId)"></i>
                                </div>
                            </div>    
                        </div>
                        <div class="tag-search">
                            <input type="text" v-model="newTag.tagName" @keyup="searchForTags()"/>
                            <button @click.prevent="newTag.tagName ? createTag() : null">Create&nbsp;New&nbsp;Tag</button>
                        </div>
                        <div class="search-tags">
                            <div class="tag-search-item" v-for="tag in searchTags">
                                <p>{{ tag.tagName }}</p>
                                <i class="fas fa-plus add-tag mini-button" @click="addTag(tag)"></i>
                            </div>
                        </div>
                    </div>

                    <div class="input-area edit-block">
                        <h3>Ingredients</h3>
                        <button v-if="meal.recipeId && meal.ingredientList.length == 0" @click="copyIngredientsFromLastTime">Copy ingredients from last time</button>
                        <div v-for="ingredient in meal.ingredientList" :key="ingredient.listOrder" class="ingredient-row">
                            <p class="list-number">&#8226;</p>
                            <div class="single-column spacer">
                                <div class="ingredient-row">
                                    <p>QTY:</p><input type="text" v-model="ingredient.quantity" class="quantity">
                                    <div class="spacer"></div>
                                </div>
                                <input type="text" class="ingredient-input" v-model="ingredient.ingredientName">
                            </div>
                            <div class="ingredient-row">
                                <div class="arrow small-button" @click="moveIngredient(-1, ingredient)">
                                    <i class="fas fa-arrow-up"></i>
                                </div>
                                <div class="arrow small-button" @click="moveIngredient(1, ingredient)">
                                    <i class="fas fa-arrow-down"></i>
                                </div>
                                <div @click="deleteIngredient(ingredient)">
                                    <i class="far fa-trash-alt small-button minus"></i>
                                </div>
                            </div> 
                       </div>
                       <div>

                    </div>
                    <div class="ingredient-column spacer border new-ingredient">
                        <div class="ingredient-row">
                            <p>QTY:</p>
                            <input type="text" class="quantity" v-model="newIngredientQuantity">
                            <div class="spacer"></div>
                        </div>
                        <input type="text" v-model="newIngredient" class="ingredient-input">
                        <button @click.prevent="addIngredient">Add Ingredient</button>
                        </div>
                    </div>
                </div>
                <input class="submit" type="submit" value="Log Meal" />
            </form> 
        </div>
        
    </body>

</template>
<script>
import MealService from '../services/MealService.js';
import RecipeService from '../services/RecipeService.js';
import ImageService from '../services/ImageService.js';
import TagService from '../services/TagService.js';

export default {
    data() {
        return {
            meal: {
                rating: 0,
                ingredientList: [],
                tags: []
            },
            showImage: false,
            imgPath: "",
            newImgId: null,
            newTag: {
                tagName: ""
            },
            searchTags: [],
            newRecipe: {
                recipeName: ""
            },
            searchRecipe: [],
            newIngredient: "",
            newIngredientQuantity: "",
            showForm: true
        }
    },
    methods: {
        async createMeal() {
            this.showForm = false;
            MealService.createMeal(this.meal).then(
                (response) => {
                    let newMeal = response.data;
                    if (!newMeal.tags) {
                        newMeal.tags = [];
                    }
                    if (this.newImgId) {
                        newMeal.imageId = this.newImgId;
                        ImageService.addImageToMeal(response.data.mealId, this.newImgId).then(
                            (res) => {
                                newMeal.img = this.imgPath;
                                this.$store.commit('CREATE_MEAL', newMeal);
                                this.$router.push({ name: 'meal-detail', params: {mealId: response.data.mealId}})
                            }
                    );
                    } else {
                        this.$store.commit('CREATE_MEAL', newMeal);
                        this.$router.push({ name: 'meal-detail', params: {mealId: response.data.mealId} })
                    }
                }
            );
            await this.resetShowFormAfterDelay();
        },
        async resetShowFormAfterDelay() {
            await new Promise(resolve => setTimeout(resolve, 3000));
            this.showForm = true;
        },
        uploadImage(event){
            this.showImage = false;
            ImageService.createImage(event.target.files[0]).then(
                (response) => {
                    this.newImgId = response.data;
                    ImageService.getImage(response.data).then(
                        (r) => {
                            const base64 = ImageService.parseImg(r);
                            this.imgPath = "data:image/png;base64," + base64;
                            this.showImage = true;
                        }
                    )
                }
            );
        },

        // TAG methods

        removeTag(id) {
            this.meal.tags = this.meal.tags.filter(
                (tag) => {
                    return tag.tagId != id;
                }
            );
        },
        createTag() {
            TagService.createTag(this.newTag).then(
                (response) => {
                    this.meal.tags.push(response.data)
                    this.$store.commit('ADD_TAG', response.data);
                    this.newTag.tagName = "";
                    this.searchTags = [];
                }
            )
        },
        addTag(tag) {
            if (!this.meal.tags) {
                this.meal.tags = [];
            }
            this.meal.tags.push(tag);
            this.newTag.tagName = "";
            this.searchTags = [];
        },
        searchForTags() {
            if (this.newTag.tagName) {
                const searchTerm = this.newTag.tagName.toLowerCase();
                this.searchTags = this.$store.state.userTags.filter(tag => 
                    tag.tagName.toLowerCase().includes(searchTerm)
                );
            } else {
                this.searchTags = [];
            }
        },

        // RECIPE methods

        searchForRecipes() {
            if (this.newRecipe.recipeName) {
                this.searchRecipe = this.$store.state.userRecipes.filter(recipe => 
                    recipe.recipeName.toLowerCase().includes(this.newRecipe.recipeName.toLowerCase())
                );
            } else {
                this.searchRecipe = [];
            }
            
        },
        addRecipe(recipe) {
            this.meal.recipeId = recipe.recipeId;
            this.meal.recipeName = recipe.recipeName;
            this.searchRecipe = null;
            this.newRecipe = {};
        },
        removeRecipe() {
            this.meal.recipeId = 0;
            this.meal.recipe = null;
        },
        createRecipe() {
            RecipeService.createRecipe(this.newRecipe).then(
                (response) => {
                    this.addRecipe(response.data);
                    this.$store.commit('CREATE_RECIPE', response.data);
                }
            )
        },

        // INGREDIENT methods

        addIngredient() {
            let ingredient = {};
            ingredient.ingredientName = this.newIngredient;
            if (!this.meal.ingredientList) {
                this.meal.ingredientList = [];
            }
            ingredient.listOrder = this.meal.ingredientList.length + 1;
            ingredient.quantity = this.newIngredientQuantity;
            this.meal.ingredientList.push(ingredient);
            this.newIngredient = "";
            this.newIngredientQuantity = "";
        },
        moveIngredient(k, ingredient) {
            if (k == 1) {
                // move down the list
                if (ingredient.listOrder != this.meal.ingredientList.length) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.meal.ingredientList[ingredient.listOrder];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
            if (k == -1) {
                // move up the list
                if (ingredient.listOrder != 1) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.meal.ingredientList[ingredient.listOrder - 2];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
        },
        deleteIngredient(ingredient) {
            let list = this.meal.ingredientList;
            if (ingredient.listOrder == list.length) {
                // case for last on list
                list.pop();
            }
            else if (ingredient.listOrder == 1) {
                // case for first on list
                for (let i = 0; i < list.length; i++) {
                    if (i != list.length - 1) {
                        list[i].ingredientName = Object.assign(list[i+1].ingredientName);
                        list[i].quantity = Object.assign(list[i+1].quantity);
                    }
                    else if (i == list.length - 1) {
                        list.pop();
                    }
                }
            }
            else {
                // case for middle of list
                for (let i = ingredient.listOrder - 1; i < list.length; i++) {
                    if (i != list.length - 1) {
                        list[i].ingredientName = Object.assign(list[i+1].ingredientName);
                        list[i].quantity = Object.assign(list[i+1].quantity);
                    }
                    else if (i == list.length - 1) {
                        list.pop();
                    }
                }
            }
            if (null) {
                
            }
        },
        copyIngredientsFromLastTime() {
            const lastMeal = this.$store.state.userMeals
                .filter(meal => meal.recipeId === this.meal.recipeId && meal.mealId !== this.meal.mealId)
                .sort((a, b) => new Date(b.dateCooked) - new Date(a.dateCooked))[0];
            if (lastMeal && lastMeal.ingredientList) {
                this.meal.ingredientList = [...lastMeal.ingredientList];
            }
        },
        copyTagsFromLastTime() {
            const lastMeal = this.$store.state.userMeals
                .filter(meal => meal.recipeId === this.meal.recipeId && meal.mealId !== this.meal.mealId)
                .sort((a, b) => new Date(b.dateCooked) - new Date(a.dateCooked))[0];
            if (lastMeal && lastMeal.tags) {
                this.meal.tags = [...lastMeal.tags];
            }
        }
    }
}
</script>

<style scoped>
    body {
        display: flex;
        flex-direction: column;
        padding: 5px;
        margin-top: 0px;
    }
    h1 {
        margin-top: 0px;
    }
    div {
        display: flex;
        flex-direction: column; 
        flex-grow: 1;
        justify-content: center;
    }
    h2, h3 {
        display: inline;
        margin: 0px;
    }
    p {
        margin: 5px;
        text-align: left;
    }
    input {
        width: 99%;
        margin-bottom: 3px;
    }
    form {
        width: 99%;      
    }
    textarea {
        resize: none;
        width: 99%;
        margin-bottom: 3px;
    }
    .submit {
        width: 100%;
    }
    .image-block {
        background-color: var(--light-1);
        padding: 10px;
        border-radius: 10px;
    }
    .image-block h3 {
        margin: 0px;
        margin-bottom: 5px;
    }
    .details {
        background-color: var(--light-1);
        padding: 10px;
        border-radius: 10px;
        margin-top: 10px;;
    }
    .edit-tags {
        display: flex;
        flex-direction: column;
    }
    .tags {
        margin-top: 5px;
        padding: 10px;
        background-color: var(--tag);
        border-radius: 10px;
        display: flex;
        flex-direction: row;
    }
    .tag-search {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        margin-top: 5px;
        margin-bottom: 5px;
        flex-wrap: nowrap;
    }
    .tag-search input {
        flex-grow: 1;
    }
    .tag-list {
        background-color: var(light-2);
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
    }
    .tag-list i {
        height: .9em;
    }
    .tag-item i:hover {
        filter: opacity(50%);
    }
    .tag-search-item i:hover {
        filter: opacity(50%);
    }
    .tag-item {
        display: flex;
        flex-wrap: nowrap;
        border: 1px solid var(--border-color);
        background-color: var(--light-1);
        border-radius: 8px;
        align-items: center;
        padding: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
    }
    .tag-item-2 {
        display: flex;
        background-color: var(--light-2);
        border-radius: 8px;
        justify-content: center;
        align-items: center;
        padding: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
        user-select: none;
    }
    .tag-item:hover {
        cursor: pointer;
    }
    .tag-search-item {
        display: flex;
        flex-direction: row;
        border: 1px solid var(--border-color);
        background-color: var(--light-1);
        border-radius: 8px;
        align-items: center;
        padding: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
        flex-grow: 0; /* Prevent expanding */
        flex-wrap: nowrap; /* Ensure single row */
    }
    .tag-search-item p {
        margin: 0; /* Ensure no margin for inline display */
        flex-shrink: 0; /* Prevent shrinking */
    }
    .tag-search-item i {
        border: 1px solid var(--border-color);
        margin-left: 5px; /* Add some space between text and icon */
        flex-shrink: 0; /* Prevent shrinking */
    }
    .tag-search-item:hover {
        cursor: pointer;
    }
    .add-tag {
        background-color: var(--light-4);
    }
    .add-tag:hover {
        background-color: opacity(50%);
    }
    .delete-tag {
        background-color: var(--light-3);
    }
    .edit-tag {
        background-color: var(--edit);
    }
    .mini-button {
        border-radius: 5px;
        padding: 4px;
    }
    .search-tags {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
    }
    input {
        margin: 0px;
    }
    .tag-block {
        margin-bottom: 10px;
    }
    .edit-block {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 15px;
        margin: 5px;
    }
    .edit-block h3 {
        margin-bottom: 5px;
    }
    .recipe-search {
        display: flex;
    }
    .recipe {
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        flex-direction: row;
        justify-content: start;
        flex-wrap: nowrap;
        align-items: center;
        padding: 5px;
        margin-top: 5px;
    }

    .recipe img {
        height: .9em;
    }
    .current-recipe {
        display: flex;
        justify-content: start;
        flex-direction: row; /* Ensure items are in a single row */
        align-items: center; /* Align items vertically */
        background-color: var(--light-1);
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 10px;
    }
    .current-recipe img {
        height: .9em;
    }
    .plus {
        background-color: var(--light-4);
    }
    .minus {
        background-color: var(--light-3);
    }
    .mini-button {
        border-radius: 5px;
        padding: 4px;
    }
    .edit-recipe h4 {
        margin: 5px;
    }
    .list-number {
        margin: 3px;
    }
    .arrow {
        width: 15px;
        padding: 5px;
        display: flex;
        justify-content: center;
        background-color: var(--light-5);
    }
    .ingredient-input {
        width: 90%;
        flex-grow: 1;
        margin-bottom: 10px;
    }
    .quantity {
        width: 20%;
        margin: 5px;
    }
    .single-column {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        align-items: start;
        justify-content: left;
    }
    .step-input {
        padding: 0px;
        margin: 10px;
        flex-grow: 1;

    }
    .ingredient-column {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        align-items: start;
        justify-content: start;
    }
    .ingredient-row {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        align-items: center;
        justify-content: start;
    }
    .new-ingredient {
        background-color: var(--light-2);
        border: 1px solid var(--border-color);
        border-radius: 10px;
        padding: 10px;
        margin: 5px;
    }
    .rating-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%; /* Fill the width */
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
    }
    .stars {
        display: flex;
        flex-direction: row; /* Ensure stars are in a single row */
        margin-right: 10px;
        color: var(--light-5);
    }
    .image {
        display: flex;
        justify-content: center;
        align-items: center;
    }
    .image img {
        max-width: 100%;
        max-height: 300px;
        object-fit: cover;
        border-radius: 10px;
    }
    .image img {
        max-width: 100%;
        max-height: 300px;
        object-fit: cover;
        border-radius: 10px;
    }
</style>