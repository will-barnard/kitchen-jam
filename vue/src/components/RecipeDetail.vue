<template>
    <Transition>
    <body>
       
        <div class="recipe-img">
            <div>
                <img :src="imgPath" />
            </div>
        </div>

       <div v-show="!editing">
           <div>
               <div class="details">
                   <div class="title">
                       <h2 >{{ staticRecipe.recipeName }}</h2>
                   </div>
                   <div class="category" v-show="staticRecipe.categoryId">
                    <h3>{{ staticRecipe.categoryName }}</h3>
                   </div>
                   <div class="subtitle">
                    <p>{{ staticRecipe.description }}</p>   
                   </div>
                   <!-- <h3>Last made here?</h3> -->

                   

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
            <div class="edit-image edit-block">
                <h3>Edit image</h3>
                <input type="file" name="file" accept="image/*" @change="uploadImage">
            </div>
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
           <div class="edit-form edit-category">
                <h3>Edit Category</h3>
                <div>
                    <div v-if="newRecipe.categoryId" class="current-category">
                        <img src="/img/minus.png" class="minus mini-button" @click="removeCategory()">
                        <div class="tag-spacer"></div>
                        <h4>{{ newRecipe.categoryName }}</h4>
                    </div>
                    <div class="category-search">
                        <input type="text" v-model="newCategory.categoryName" @keyup="searchForCategory()"/>
                        <button @click="createCategory()">Create New Category</button>
                    </div>
                    <div class="category-search-results" v-if="searchCategory">
                        <div v-for="category in searchCategory" class="category-item">
                            <img src="/img/plus.png" class="plus mini-button" @click="addCategory(category)"/>
                            <div class="tag-spacer"></div>
                            <p>{{ category.categoryName }}</p>
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
        <div class="meals" v-if="mealList && !editing">
            <h3>Meals following this recipe</h3>
            <MealCard v-for="meal in mealList" :key="meal.mealId" :meal="meal"></MealCard>
       </div>       
   </body>
   </Transition>
</template>

<script>
import RecipeService from '../services/RecipeService.js'
import Tag from './Tag.vue';
import MealCard from '../components/MealCard.vue';
import CategoryService from '../services/CategoryService.js';
import ImageService from '../services/ImageService.js';

export default {
    props: ['recipe', 'loading'],
    components: {Tag, MealCard},
    data() {
        return {
            editing: false,
            showImage: false,
            imgPath: "",
            newRecipe: {},
            staticRecipe: {},
            deleting: false,
            newTag: {},
            newCategory: {},
            searchCategory: [],
            mealList: []
        }
    },
    created() {
        this.mealList = this.$store.state.userMeals.filter(
            (meal) => {
                return meal.recipeId == this.recipe.recipeId;
            }
        )
        this.staticRecipe = this.cloneRecipe(this.recipe);
        this.newRecipe = this.cloneRecipe(this.staticRecipe);
        if (this.recipe.imageId == 0 || this.recipe.imageId == null) {
            this.imgPath = "../img/placeholder.jpeg";
            this.showImage = true;
        } else {
            this.imgPath = this.recipe.img;
            this.showImage = true;
        }

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
                newRecipe.categoryName = recipe.categoryName;
                newRecipe.mealList = recipe.mealList;
                newRecipe.img = recipe.img;

                newRecipe.categories = recipe.categories;

            }
            return newRecipe;
        },
        uploadImage(event){
            this.showImage = false;
            
            if (this.recipe.imageId == 0 || this.recipe.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        this.newRecipe.imgageId = response.data;
                        ImageService.addImageToRecipe(this.recipe.recipeId, response.data).then(
                            () => {
                                ImageService.getImage(response.data).then(
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
                        this.newRecipe.imgageId = id;
                        ImageService.updateRecipeImage(this.recipe.recipeId, id).then(
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
        searchForCategory() {
            if (this.newCategory.categoryName) {
                CategoryService.searchCategory(this.newCategory).then(
                    (response) => {
                        this.searchCategory = response.data;
                    }
                )
            } else {
                this.searchCategory = [];
            }
            
        },
        addCategory(category) {
            this.newRecipe.categoryId = category.categoryId;
            this.newRecipe.categoryName = category.categoryName;
            this.newCategory.categoryName = "";
            this.searchCategory = [];
        },
        removeCategory() {
            this.newRecipe.categoryId = 0;
            this.newRecipe.categoryName = "";
        },
        createCategory() {
            CategoryService.createCategory(this.newCategory).then(
                (response) => {
                    this.newRecipe.categoryId = response.data.categoryId;
                    this.newRecipe.categoryName = response.data.categoryName;
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
    .recipe-img {
        margin-top: 10px;
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
    .mini-button {
        border-radius: 5px;
        padding: 4px;
    }
    .current-category img {
        height: .9em;
    }
    .current-category {
        display: flex;
        background-color: var(--light-1);
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 10px;
    }
    .plus {
        background-color: var(--light-4);
    }
    .minus {
        background-color: var(--light-3);
    }
    .tag-spacer {
        width: 5px;
    }
    .category {
        text-align: center;
        background-color: var(--light-6);
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 10px;
    }
    .category-item {
        display: flex;
        margin-bottom: 5px;
        background-color: var(--light-1);
        padding: 5px;
        border-radius: 10px;
    }
    .category-item img {
        height: .9em;
    }
    .edit-block {
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 15px;
        margin: 5px;
    }
    .edit-image h3 {
        margin-bottom: 5px;
    }
    .meals h3 {
        background-color: var(--light-1);
        border-radius: 10px;
        margin-top: 30px;
        margin-bottom: 5px;
        padding: 10px;
    }
</style>