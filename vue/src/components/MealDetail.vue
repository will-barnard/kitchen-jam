<template>
    <Transition>
     <body v-show="showImage">

        <div class="meal-img">
                <div>
                    <img :src="imgPath" />
                </div>
        </div>
        
        <div class="display" v-show="!editing">
            <div>
                <div class="details">

                    <div class="title">
                        <h2 >{{ staticMeal.mealName }}</h2>
                        <h3 v-if="staticMeal.recipeId">{{ staticMeal.recipeName }}</h3>
                        <div class="subtitle">
                            <h4 class="comment">{{ staticMeal.mealComment }}</h4>
                            <h4 class="date">{{ formatDate(staticMeal.dateCooked) }}</h4>
                        </div>
                    </div>

                    <div class="tags">
                        <p v-if="!staticMeal.tags">No tags yet</p>
                        <div class="tag-list">
                            <div  v-for="tag in staticMeal.tags">
                                <div class="tag-item-2">
                                    <p>{{tag.tagName}}</p>
                                </div>    
                            </div>
                        </div>
                    </div>

                    <div class="widgets">
                        <div class="cooktime">
                            <img src="/img/clock.png" />
                            <p>{{ staticMeal.cookTime }} min</p>
                        </div>
                        <div class="rating">
                            <p>{{ staticMeal.rating }} / 10 Rating</p>
                        </div>
                    </div>

                    <div class="foot">
                        <div class="ingredients">
                            <h3>Ingredients:</h3>
                            <p>{{ staticMeal.ingredients }}</p>
                        </div>
                        <div class="notes">
                            <h3>Notes:</h3>
                            <p>{{ staticMeal.notes }}</p>
                        </div>
            
                    </div>

                </div>            
            </div>
        </div>

        <div class="edit" v-show="editing">

            <div class="edit-image edit-block">
                <h3>Edit image</h3>
                <input type="file" name="file" accept="image/*" @change="uploadImage">
            </div>

            <div class="edit-recipe edit-block">
                <h3>Edit recipe</h3>
                <div v-if="newMeal.recipeId" class="current-recipe">
                    <img src="/img/minus.png" class="minus mini-button" @click="removeRecipe()">
                    <div class="tag-spacer"></div>
                    <h4>{{ newMeal.recipeName }}</h4>
                </div>
                <div class="recipe-search">
                    <input type="text" v-model="newRecipe.recipeName" @keyup="searchForRecipes()"/>
                    <button @click="createRecipe()">Create New Recipe</button>
                </div>
                <div class="recipe-search-results">
                    <div v-for="recipe in searchRecipe" class="recipe">
                        <img src="/img/plus.png" class="plus mini-button" @click="addRecipe(recipe)"/>
                        <div class="tag-spacer"></div>
                        <p>{{ recipe.recipeName }}</p>
                    </div>
                </div>
            </div>

            <div class="edit-tags edit-block">
                <h3>Edit tags</h3>
               
                <div>
                    <p v-if="!newMeal.tags">no tags</p>

                    <div class="tag-list">
                        <div  v-for="tag in staticMeal.tags">
                            <div class="tag-item">
                                <p>{{tag.tagName}}</p>
                                <div class="tag-spacer"></div>
                                <img class="delete-tag mini-button" src="/img/trash.png" @click="removeTag(tag.tagId)"/>
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
                        <img class="add-tag mini-button" src="/img/check.png" @click="addTag(tag)"/>
                        <div class="tag-spacer"></div>
                        <img class="edit-tag mini-button" src="/img/edit.png" />
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
                        
                        <div class="edit-rating">
                            <label>Rating</label>
                            <select v-model="newMeal.rating">
                                <option value="0">0</option>
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                                <option value="5">5</option>
                                <option value="6">6</option>
                                <option value="7">7</option>
                                <option value="8">8</option>
                                <option value="9">9</option>
                                <option value="10">10</option>
                            </select>
                        </div> 

                        <div class="">
                            <label>Cook time (min)</label>
                            <input type="number" v-model="newMeal.cookTime"/>
                        </div>

                    </div> 
                    
                </div>
            </form>
        </div>

        <div class="delete" v-if="deleting">
            <p id="delete-check">Are you sure you want to delete? This cannot be undone</p>
            <h2 class="yes-delete" v-on:click="deleteMeal()">Delete</h2>
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

export default {
    props: ['meal', 'loading'],
    components: {Tag},
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
            searchRecipe: []
        }
    },
    created() {
        this.staticMeal = this.cloneMeal(this.meal);
        this.newMeal = this.cloneMeal(this.staticMeal);
        if (this.meal.imageId == 0 || this.meal.imageId == null) {
            this.imgPath = "../img/placeholder.jpeg";
            this.showImage = true;
        } else {
            ImageService.getImage(this.meal.imageId).then(
                (res) => {
                    const base64 = ImageService.parseImg(res);
                    this.imgPath = "data:image/png;base64," + base64;
                    this.showImage = true;
                }
            )
        }
    },
    methods: {
        cancelEdit() {
            this.editing = false;
            this.newMeal = this.cloneMeal(this.staticMeal);
        },
        saveEdit() {
            MealService.updateMeal(this.newMeal).then(
                (response) => {
                    this.staticMeal = response.data;
                    this.newMeal = this.staticMeal;
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
            MealService.deleteMeal(this.meal.mealId).then(
                () => {
                    this.$router.push({name: 'meal-log'})
                }
            )
        },
        cloneMeal(meal) {
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
                newMeal.rating = meal.rating;
                newMeal.imageId = meal.imageId;
                newMeal.tags = meal.tags;
            }
            return newMeal;
        },
        uploadImage(event){
            this.showImage = false;
            
            if (this.meal.imageId == 0 || this.meal.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                (response) => {
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
                    ImageService.updateMealImage(this.meal.mealId, response.data).then(
                        (res) => {
                            ImageService.getImage(id).then(
                                (r) => {
                                    const base64 = ImageService.parseImg(r);
                                    this.imgPath = "data:image/png;base64," + base64;
                                    this.showImage = true;
                                }
                            )
                        });
                    }
                ) 
                }
        },
        formatDate(date) {
            return UtilityService.formatDate(date);
        },
        removeTag(id) {
            TagService.removeTagFromMeal(this.meal.mealId, id).then(
                () => {
                    this.staticMeal.tags = this.staticMeal.tags.filter(
                        (tag) => {
                            return tag.tagId != id;
                        }
                    );
                }
            )
        },
        createTag() {
            TagService.createTag(this.newTag).then(
                (response) => {
                    this.staticMeal.tags.push(response.data)
                    TagService.addTagToMeal(this.meal.mealId, response.data.tagId)
                }
            )
        },
        addTag(tag) {
            this.staticMeal.tags.push(tag);
            TagService.addTagToMeal(this.meal.mealId, tag.tagId);
        },
        searchForTags() {
            TagService.searchTags(this.newTag).then(
                (response) => {
                    this.searchTags = response.data;
                }
            );
        },
        searchForRecipes() {
            RecipeService.searchRecipes(this.newRecipe).then(
                (response) => {
                    this.searchRecipe = response.data;
                }
            )
        },
        addRecipe(recipe) {
            this.newMeal.recipeId = recipe.recipeId;
            this.newMeal.recipeName = recipe.recipeName;
            this.searchRecipe = null;
            this.newRecipe = "";
        },
        removeRecipe() {
            this.newMeal.recipeId = 0;
            this.newMeal.recipe = null;
        },
        createRecipe() {
            RecipeService.createRecipe(this.newRecipe).then(
                (response) => {
                    this.addRecipe(response.data);
                }
            )
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
    .edit-form {
        display: flex;
        flex-direction: column;
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
    .tag-list img {
        height: .9em;
    }
    .tag-item img:hover {
        filter: opacity(50%);
    }
    .search-tags {
        display: flex;
        white-space: nowrap;
        flex-wrap: wrap;
    }
    .search-tags img {
        height: .9em;
    }
    .tag-spacer {
        width: 5px;
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
    .tag-search-item img:hover {
        filter: opacity(50%);
    }
    .tag-search-item img {
        border: 1px solid var(--border-color);
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


</style>