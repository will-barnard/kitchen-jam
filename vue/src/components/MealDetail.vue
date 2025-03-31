<template>
    <Transition>
     <body v-show="showImage">

        <div class="meal-img">
                <div>
                    <img :src="imgPath" />
                </div>
        </div>

        <div v-show="!editing">
            <MealDetailCard :meal="staticMeal" :editable="true"/>
        </div>

        <div class="edit" v-show="editing">

            <div class="edit-image edit-block">
                <h3>Image</h3>
                <input type="file" name="file" accept="image/*" @change="uploadImage">
            </div>
            <div class="edit-block">
                <Transition name="quickFade">
                    <div v-if="newMeal.public">
                        <div class="single-row">
                            <h3>Sharing</h3>
                            <div class="spacer"></div>
                            <p>Public &#127760;</p>
                        </div>
                        <div class="single-row">
                            <button @click.prevent="unsecuredCopyToClipboard(newMeal.publicUrl)">Copy URL</button>
                            <div class="spacer">
                                <Transition name="quickFade">
                                    <p v-show="copiedURL">&nbsp;Link copied!</p>
                                </Transition></div>
                            <button @click.prevent="makePrivate()">Make private</button>
                        </div>
                        
                    </div>
                </Transition>
                <Transition name="quickFade">
                    <div v-if="!newMeal.public">
                        <div class="single-row">
                            <h3>Sharing</h3>
                            <div class="spacer"></div>
                            <p>Private &#128274;</p>
                        </div>
                        <div class="single-row">
                            <div class="spacer"></div>
                            <button @click.prevent="makePublic()">Make public</button>
                        </div>
                    </div>
                </Transition>
            </div>

            <div class="edit-recipe edit-block">
                <h3>Recipe</h3>
                <div v-if="newMeal.recipeId" class="current-recipe">
                    <i class="fas fa-minus minus mini-button" @click="removeRecipe()"></i>
                    <div class="tag-spacer"></div>
                    <h4>{{ newMeal.recipeName }}</h4>
                </div>
                <div class="recipe-search">
                    <input type="text" v-model="newRecipe.recipeName" @keyup="searchForRecipes()"/>
                    <button @click="createRecipe()">Create New Recipe</button>
                </div>
                <div class="recipe-search-results">
                    <div v-for="recipe in searchRecipe" class="recipe">
                        <i class="fas fa-plus plus mini-button" @click="addRecipe(recipe)"></i>
                        <div class="tag-spacer"></div>
                        <p>{{ recipe.recipeName }}</p>
                    </div>
                </div>
            </div>

            <div class="edit-tags edit-block">
                <h3>Tags</h3>
               
                <div>
                    <p v-if="!newMeal.tags">no tags</p>

                    <div class="tag-list">
                        <div  v-for="tag in newMeal.tags">
                            <div class="tag-item">
                                <p>{{tag.tagName}}</p>
                                <div class="tag-spacer"></div>
                                <i class="far fa-trash-alt delete-tag mini-button" @click="removeTag(tag.tagId)"></i>
                            </div>    
                        </div>
                    </div>    

                </div>

                <div class="tag-search">
                    <input type="text" v-model="newTag.tagName" @keyup="searchForTags()"/>
                    <button @click="createTag()">Create New Tag</button>
                </div>

                <div class="search-tags">
                    <div class="tag-search-item" v-for="tag in searchTags">
                        <p>{{ tag.tagName }}</p>
                        
                        <div class="tag-spacer"></div>
                        <i class="fas fa-plus add-tag mini-button" @click="addTag(tag)"></i>
                    </div>
                </div>
                
            </div>

            <form>
                <div class="edit-form edit-block">
                    
                    <h3>Edit details</h3>

                    <label>Name</label>
                    <input type="text" v-model="newMeal.mealName">
                    
                    
                    <label>Date cooked</label>
                    <input type="date" v-model="newMeal.dateCooked"/>
                    
                    
                    <label>Comment</label>
                    <input type="text" v-model="newMeal.mealComment"/>
                    
                    <label>Ingredient list</label>
                    <input type="text" v-model="newMeal.ingredients"/>
                    <label>Notes</label>
                    <textarea v-model="newMeal.notes"></textarea>

                    <div class="edit-widgets">
                        
                        <div class="">
                            <label>Cook time (min)</label>
                            <input type="number" v-model="newMeal.cookTime"/>
                        </div>

                        <div class="edit-rating">
                            <label>Rating</label>
                            <div class="rating-container">
                                <div class="stars">
                                    <i v-for="n in 5" :key="n" :class="{
                                        'fas fa-star': newMeal.rating >= n * 2,
                                        'fas fa-star-half-alt': newMeal.rating >= (n * 2) - 1 && newMeal.rating < n * 2,
                                        'far fa-star': newMeal.rating < (n * 2) - 1
                                    }"></i>
                                </div>
                                <input type="range" min="0" max="10" step="1" v-model="newMeal.rating"/>
                            </div>
                        </div> 

                        

                    </div> 
                    
                </div>
       </form>
            <div class="edit-form">
                   <div class="input-area">
                        <h3>Ingredients</h3>
                        <button v-if="newMeal.recipeId" @click="copyIngredientsFromLastTime">Copy ingredients from last time</button>
                        <div v-for="ingredient in newMeal.ingredientList" :key="ingredient.listOrder" class="single-row">
                            <p class="list-number">&#8226;</p>
                            <div class="single-column spacer">
                                <div class="single-row">
                                    <p>QTY:</p><input type="text" v-model="ingredient.quantity" class="quantity">
                                    <div class="spacer"></div>
                                </div>
                                <input type="text" class="ingredient-input" v-model="ingredient.ingredientName">
                            </div>
                            <div class="single-row">
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
                    <div class="ingredient-column spacer border">
                        <div class="single-row">
                            <p>QTY:</p>
                            <input type="text" class="quantity" v-model="newIngredientQuantity">
                            <div class="spacer"></div>
                        </div>
                        <input type="text" v-model="newIngredient" class="ingredient-input">
                        <button @click.prevent="addIngredient">Add Ingredient</button>
                        </div>
                    </div>
                </div>
        </div>

        <div class="delete" v-if="deleting">
            <p id="delete-check">Are you sure you want to delete? This cannot be undone</p>
            <h2 class="yes-delete" v-on:click="deleteMeal()">Delete</h2>
            <h2 class="cancel-delete" v-on:click="deleting = false">Cancel</h2>
        </div>

        <div class="controls" v-show="!deleting">
            <div class="link-button button" v-show="staticMeal.public" @click="unsecuredCopyToClipboard(newMeal.publicUrl)">
                <i class="fas fa-link"></i>
            </div>
            <div class="spacer"><p v-show="copiedURL">Link copied!</p></div>
            <div class="edit-button button" v-if="!editing" v-on:click="editing=true">
                <i class="fas fa-edit"></i>
            </div>
            <div class="trash button" v-if="!editing" v-on:click="deleteButton()">
                <i class="far fa-trash-alt"></i>
            </div>
            <div class="undo button" v-if="editing" v-on:click="cancelEdit()">
                <i class="fas fa-undo"></i>
            </div>
            <div class="check button" v-if="editing" v-on:click="saveEdit()">
                <i class="fas fa-check"></i>
            </div>
        </div>

    </body>
    </Transition>
</template>

<script>
import MealService from '../services/MealService.js'
import Tag from './Tag.vue';
import ImageService from '../services/ImageService.js';
import UtilityService from '../services/UtilityService.js';
import TagService from '../services/TagService.js';
import RecipeService from '../services/RecipeService.js';
import MealDetailCard from './MealDetailCard.vue';

function cloneMeal(meal) {
    let newMeal = {};
    if (meal) {
        newMeal.mealId = meal.mealId;
        newMeal.userId = meal.userId;
        newMeal.recipeId = meal.recipeId;
        newMeal.recipeName = meal.recipeName;
        newMeal.mealName = meal.mealName;
        newMeal.mealComment = meal.mealComment;
        newMeal.dateCreated = meal.dateCreated;
        newMeal.dateCooked = meal.dateCooked;
        newMeal.lastModified = meal.lastModified;
        newMeal.cookTime = meal.cookTime;
        newMeal.notes = meal.notes;
        newMeal.ingredients = meal.ingredients;
        newMeal.ingredientList = JSON.parse(JSON.stringify(meal.ingredientList));
        newMeal.rating = meal.rating;
        newMeal.imageId = meal.imageId;
        newMeal.tags = meal.tags;
        newMeal.img = meal.img;
        newMeal.public = meal.public;
        newMeal.publicUrl = meal.publicUrl;
    }
    return newMeal;
}

export default {
    props: ['meal', 'loading'],
    components: {Tag, MealDetailCard},
    data() {
        return {
            editing: false,
            deleting: false,
            showImage: false,
            imgPath: "",
            newMeal: {},
            staticMeal: {},
            newTag: {},
            searchTags: [],
            newRecipe: {},
            searchRecipe: [],
            newIngredient: "",
            newIngredientQuantity: "",
            copiedURL: false
        }
    },
    created() {
        this.staticMeal = cloneMeal(this.meal);
        this.newMeal = cloneMeal(this.staticMeal);


        // img handling
        if (this.meal.imageId == 0 || this.meal.imageId == null) {
            this.imgPath = "../img/placeholder.jpeg";
            this.showImage = true;
        } else {
            this.imgPath = this.meal.img;
            this.showImage = true;
        }
    },
    methods: {

        // EDIT method

        cancelEdit() {
            this.editing = false;
            this.newMeal = cloneMeal(this.staticMeal);
            
        },
        saveEdit() {
            MealService.updateMeal(this.newMeal).then(
                (response) => {
                    let newMeal = response.data;
                    if (newMeal.imageId) {
                        ImageService.getImage(newMeal.imageId).then(
                            (r) => {
                                const base64 = ImageService.parseImg(r);
                                newMeal.img = "data:image/png;base64," + base64;
                            }
                        )
                    }
                    this.$store.commit('UPDATE_MEAL', newMeal);
                    this.staticMeal = cloneMeal(response.data);
                    this.newMeal = cloneMeal(this.staticMeal);
                    this.editing = false;
                }
            )
        },
        deleteButton() {
            if (!this.deleting) {
                this.deleting = true;
            }
        },
        deleteMeal() {
            this.$store.commit('DELETE_MEAL', this.meal.mealId);
            this.$router.push({name: 'meal-log'})
        },
        // remove??
        formatDate(date) {
            return UtilityService.formatDate(date);
        },

        // TAG methods

        removeTag(id) {
            this.newMeal.tags = this.newMeal.tags.filter(
                (tag) => {
                    return tag.tagId != id;
                }
            );
        },
        createTag() {
            TagService.createTag(this.newTag).then(
                (response) => {
                    this.newMeal.tags.push(response.data)
                    $store.state.commit('ADD_TAG', response.data);
                    this.newTag.tagName = "";
                    this.searchTags = [];
                }
            )
        },
        addTag(tag) {
            this.newMeal.tags.push(tag);
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
            this.newMeal.recipeId = recipe.recipeId;
            this.newMeal.recipeName = recipe.recipeName;
            this.searchRecipe = null;
            this.newRecipe = {};
        },
        removeRecipe() {
            this.newMeal.recipeId = 0;
            this.newMeal.recipe = null;
        },
        createRecipe() {
            RecipeService.createRecipe(this.newRecipe).then(
                (response) => {
                    this.addRecipe(response.data);
                    this.$store.state.commit('CREATE_RECIPE', response.data);
                }
            )
        },

        // INGREDIENT methods

        addIngredient() {
            let ingredient = {};
            ingredient.ingredientName = this.newIngredient;
            ingredient.listOrder = this.newMeal.ingredientList.length + 1;
            ingredient.quantity = this.newIngredientQuantity;
            this.newMeal.ingredientList.push(ingredient);
            this.newIngredient = "";
            this.newIngredientQuantity = "";
        },
        moveIngredient(k, ingredient) {
            if (k == 1) {
                // move down the list
                if (ingredient.listOrder != this.newMeal.ingredientList.length) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.newMeal.ingredientList[ingredient.listOrder];
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
                    let pivotIngredient = this.newMeal.ingredientList[ingredient.listOrder - 2];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
        },
        deleteIngredient(ingredient) {
            let list = this.newMeal.ingredientList;
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
                .filter(meal => meal.recipeId === this.newMeal.recipeId && meal.mealId !== this.newMeal.mealId)
                .sort((a, b) => new Date(b.dateCooked) - new Date(a.dateCooked))[0];
            if (lastMeal && lastMeal.ingredientList) {
                this.newMeal.ingredientList = [...lastMeal.ingredientList];
            }
        },


        // PUBLIC methods
        // TODO implement store here?

        makePublic() {
            MealService.makePublic(this.meal.mealId).then(
                (response) => {
                    this.newMeal.publicUrl = response.data;
                    this.newMeal.public = true;
                    this.staticMeal.publicUrl = response.data;
                    this.staticMeal.public = true;

                }
            )
        },
        makePrivate() {
            MealService.makePrivate(this.meal.mealId).then(
                () => {
                    this.newMeal.publicUrl = null;
                    this.newMeal.public = false;
                    this.staticMeal.publicUrl = null;
                    this.staticMeal.public = false;
                }
            );
        },

        // TODO FIX IMG method

        uploadImage(event){
            this.showImage = false;
            
            if (this.meal.imageId == 0 || this.meal.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        ImageService.addImageToMeal(this.meal.mealId, response.data).then(
                            () => {
                                ImageService.getImage(id).then(
                                    (r) => {
                                        const base64 = ImageService.parseImg(r);
                                        this.imgPath = "data:image/png;base64," + base64;
                                        this.showImage = true;
                                    }
                                )
                            }
                        );
                    }
                ) 
            } else {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        let id = response.data;
                        ImageService.updateMealImage(this.meal.mealId, id).then(
                            () => {
                                ImageService.getImage(id).then(
                                    (r) => {
                                        const base64 = ImageService.parseImg(r);
                                        this.imgPath = "data:image/png;base64," + base64;
                                        this.showImage = true;
                                    }
                                )
                            }
                        );
                    }
                ) 
            }
        },
        async copyURL(mytext) {
            try {
                await navigator.clipboard.writeText(mytext);
                this.copiedURL = true;
                setTimeout(() => this.copiedURL = false, 2000);
            } catch($e) {
                console.log($e)
            }
        },
        unsecuredCopyToClipboard(text) {
            const textArea = document.createElement("textarea");
            textArea.value = text;
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            try {
                document.execCommand('copy');
                this.copiedURL = true;
                setTimeout(() => this.copiedURL = false, 2000);
            } catch (err) {
                console.error('Unable to copy to clipboard', err);
            }
            document.body.removeChild(textArea);
        }
    }
}
</script>

<style scoped>
    body p, h1, h2, h3, h4 {
        margin: 0px;
    }
    .meal-img {
        margin-top: 15px;
        overflow: hidden;
        text-align: center;
        margin-bottom: 15px;
        margin-left: 5px;
        margin-right: 5px;
    }
    .meal-img img {
        width: 100%;
        border-radius: 15px;
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
    .controls i {
        font-size: 2em; /* Increase the size of the icons */
        margin: 0 10px; /* Add some margin for spacing */
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
        display: flex;
        justify-content: center;
        align-items: center;
        text-align: center; /* Center the icons */
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
    .link-button {
        background-color: var(--light-7);
        margin-left: 10px;
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
    .edit-block {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 15px;
        margin: 5px;
    }
    .edit-block h3 {
        margin-bottom: 5px;
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
    .edit-form label, input {
        margin-bottom: 5px;
    }
    .edit-widgets {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        margin-top: 5px;
        margin-bottom: 5px;
    }
    .edit-widget {
        width: 65%;
    } 
    .tag-search {
        display: flex;
    }
    .tag-search input {
        flex-grow: 1;
    }
    .recipe-search {
        display: flex;
    }
    .edit-block input {
        margin: 0px;
        flex-grow: 1;
    }
    

    

    .tag {
        border: 1px solid var(--border-color);
        padding: 3px;
        padding-left: 15px;
        padding-right: 15px;
        padding-top: 3px;
        padding-bottom: 3px;
        border-radius: 50px;
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
        border: 1px solid var(--border-color);
        background-color: var(--light-1);
        border-radius: 8px;
        align-items: center;
        padding: 5px;
        margin-right: 5px;
        margin-bottom: 5px;
    }
    .tag-search-item:hover {
        cursor: pointer;
    }
    .tag-search-item i {
        border: 1px solid var(--border-color);
    }
    .search-tags {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        align-items: center;
        justify-content: center;
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

    .recipe {
        background-color: var(--light-1);
        border-radius: 10px;
        display: flex;
        align-items: center;
        padding: 5px;
        margin-top: 5px;
    }

    .recipe img {
        height: .9em;
    }
    .current-recipe {
        display: flex;
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
        background-color: var(--light-3);
        border-radius: 10px;
        padding: 10px;
        margin: 0px;
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
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
    }
    .comment {
        margin-left: 0px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        flex-grow: 1;
    }
    .date {
        text-align: right;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 10px;
        margin-right: 0px;
        height: 100%;
        margin-left: 5px;
    }
    .widgets p {
        margin: 0px;
    }
    .widgets {
        display: flex;
        margin-top: 15px;
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
    .foot {
        margin-top: 5px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
    }
    .foot h3, p {
        margin-bottom: 5px;
    }
    .foot h3 {
        margin-left: 10px;
    }
    .foot p {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 10px;
    }
    .ingredients {
        margin-bottom: 10px;
    }


    .v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
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
    .button {
        width: 15vw;
        /* border: 1px solid var(--border-color); */
        border-radius: 10px;
        padding: 5px;
        margin-right: 5px;
    }
    .ingredient-column {
        display: flex;
        flex-direction: column;
        flex-wrap: nowrap;
        align-items: start;
        justify-content: start;
    }
    .border {
        border: 1px solid;
        padding: 10px;
        border-radius: 10px;;    
    }
    .rating-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 70%;
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
    }
    .stars {
        margin-right: 10px;
        color: var(--light-5)
    }
    .edit-rating {
        margin-top: 10px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;
    }

</style>