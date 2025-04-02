<template>
    <div>
        
        <h1>New Recipe</h1>

        <div class="image-block">
            <div class="edit-form">
                <div class="image" v-if="showImage">
                    <img :src="imgPath" />
                </div>
                <div class="add-image">
                    <h3>Add image</h3>
                    <input type="file" name="file" accept="image/*" @change="uploadImage">
                </div>
            </div>
            
        </div>

        <div class="edit-block">
            <form v-on:submit.prevent="createRecipe()">
                <div class="">
                    <div class="edit-form">
                        <h3>Recipe Details</h3>
                        <label>Name</label><input type="text" v-model="recipe.recipeName">
                        <label>Description</label><input type="text" v-model="recipe.description"/>
                    </div>
                </div>

                <div class="edit-form edit-category">
                    <h3>Category</h3>
                    <div>
                        <div v-if="recipe.categoryId" class="current-category">
                            <i class="fas fa-minus minus mini-button" @click="removeCategory()"></i>
                            <div class="tag-spacer"></div>
                            <h4>{{ recipe.categoryName }}</h4>
                        </div>
                        <div class="category-search">
                            <input type="text" v-model="newCategory.categoryName" @keyup="searchForCategory()"/>
                            <button @click="createCategory()">Create New Category</button>
                        </div>
                        <div class="category-search-results" v-if="searchCategory">
                            <div v-for="category in searchCategory" class="category-item">
                                <i class="fas fa-plus plus mini-button" @click="addCategory(category)"></i>
                                <div class="tag-spacer"></div>
                                <p>{{ category.categoryName }}</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="edit-form">
                    <h3>Ingredients</h3>
                    <div v-for="ingredient in recipe.ingredientList" :key="ingredient.listOrder" class="ingredient-row">
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
                    <div class="ingredient-column spacer border">
                        <div class="ingredient-row">
                            <p>QTY:</p>
                            <input type="text" class="quantity" v-model="newIngredientQuantity">
                            <div class="spacer"></div>
                        </div>
                        <input type="text" v-model="newIngredient" class="ingredient-input">
                        <button @click.prevent="addIngredient">Add Ingredient</button>
                    </div>
                </div>

                <div class="edit-form">
                    <h3>Steps</h3>
                    <div v-for="step in recipe.stepList" :key="step.stepOrder" class="single-row">
                        <p class="list-number">{{ step.stepOrder }}</p>
                        <textarea class="step-input" v-model="step.stepDescription"></textarea>
                        <div class="single-row">
                            <div class="arrow small-button" @click="moveStep(-1, step)">
                                <i class="fas fa-arrow-up"></i>
                            </div>
                            <div class="arrow small-button" @click="moveStep(1, step)">
                                <i class="fas fa-arrow-down"></i>
                            </div>
                            <div @click="deleteStep(step)">
                                <i class="far fa-trash-alt small-button minus"></i>
                            </div>
                        </div> 
                    </div>
                    <input type="text" v-model="newStep">
                    <button @click.prevent="addStep">Add Step</button>
                </div>

                <input type="submit" value="Create Recipe" class="submit"/>
            </form> 
        </div>    
    </div>
</template>

<script>
import RecipeService from '../services/RecipeService.js'
import CategoryService from '../services/CategoryService.js';
import ImageService from '../services/ImageService.js';

export default {
    data() {
        return {
            recipe: {
                ingredientList: [],
                stepList: []
            },
            newCategory: {},
            searchCategory: [],
            newStep: "",
            newIngredient: "",
            newIngredientQuantity: "",
            showImage: false,
            imgPath: "",
            newImgId: null
        }
    },
    methods: {
        createRecipe() {
            RecipeService.createRecipe(this.recipe).then(
                (response) => {
                    let newRecipe = response.data;
                    if (this.newImgId) {
                        newRecipe.imageId = this.newImgId;
                        ImageService.addImageToRecipe(newRecipe.recipeId, this.newImgId).then(
                            (res) => {
                                newRecipe.img = this.imgPath;
                                this.$store.commit('CREATE_RECIPE', newRecipe);
                                this.$router.push({name: 'recipe-detail', params: {recipeId: newRecipe.recipeId}})
                            }
                    );
                    }
                    this.$store.commit('CREATE_RECIPE', newRecipe)
                    this.$router.push({name: 'recipe-detail', params: {recipeId: newRecipe.recipeId}})
                }
            )
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
            this.recipe.categoryId = category.categoryId;
            this.recipe.categoryName = category.categoryName;
            this.newCategory.categoryName = "";
            this.searchCategory = [];
        },
        removeCategory() {
            this.recipe.categoryId = 0;
            this.recipe.categoryName = "";
        },
        createCategory() {
            CategoryService.createCategory(this.newCategory).then(
                (response) => {
                    this.recipe.categoryId = response.data.categoryId;
                    this.recipe.categoryName = response.data.categoryName;
                }
            )
        },
        addStep() {
            let step = {};
            step.stepDescription = this.newStep;
            step.stepOrder = this.recipe.stepList.length + 1;
            this.recipe.stepList.push(step);
            this.newStep = "";
        },
        moveStep(k, step) {
            if (k == 1) {
                if (step.stepOrder != this.recipe.stepList.length) {
                    let moveStep = Object.assign(step.stepDescription);
                    let pivotStep = this.recipe.stepList[step.stepOrder];
                    step.stepDescription = pivotStep.stepDescription;
                    pivotStep.stepDescription = moveStep;
                }
            }
            if (k == -1) {
                if (step.stepOrder != 1) {
                    let moveStep = Object.assign(step.stepDescription);
                    let pivotStep = this.recipe.stepList[step.stepOrder - 2];
                    step.stepDescription = pivotStep.stepDescription;
                    pivotStep.stepDescription = moveStep;
                }
            }
        },
        deleteStep(step) {
            let list = this.recipe.stepList;
            if (step.stepOrder == list.length) {
                list.pop();
            }
            else if (step.stepOrder == 1) {
                for (let i = 0; i < list.length; i++) {
                    if (i != list.length - 1) {
                        list[i].stepDescription = Object.assign(list[i+1].stepDescription);
                    }
                    else if (i == list.length - 1) {
                        list.pop();
                    }
                }
            }
            else {
                for (let i = step.stepOrder - 1; i < list.length; i++) {
                    if (i != list.length - 1) {
                        list[i].stepDescription = Object.assign(list[i+1].stepDescription);
                    }
                    else if (i == list.length - 1) {
                        list.pop();
                    }
                }
            }
        },
        addIngredient() {
            let ingredient = {};
            ingredient.ingredientName = this.newIngredient;
            ingredient.listOrder = this.recipe.ingredientList.length + 1;
            ingredient.quantity = this.newIngredientQuantity;
            this.recipe.ingredientList.push(ingredient);
            this.newIngredient = "";
            this.newIngredientQuantity = "";
        },
        moveIngredient(k, ingredient) {
            if (k == 1) {
                if (ingredient.listOrder != this.recipe.ingredientList.length) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.recipe.ingredientList[ingredient.listOrder];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
            if (k == -1) {
                if (ingredient.listOrder != 1) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.recipe.ingredientList[ingredient.listOrder - 2];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
        },
        deleteIngredient(ingredient) {
            let list = this.recipe.ingredientList;
            if (ingredient.listOrder == list.length) {
                list.pop();
            }
            else if (ingredient.listOrder == 1) {
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
                for (let i = step.stepOrder - 1; i < list.length; i++) {
                    if (i != list.length - 1) {
                        list[i].ingredientName = Object.assign(list[i+1].ingredientName);
                        list[i].quantity = Object.assign(list[i+1].quantity);
                    }
                    else if (i == list.length - 1) {
                        list.pop();
                    }
                }
            }
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
        }
    }
}
</script>

<style scoped>
    h1 {
        margin-top: 0px;
    }
    .container {
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
    .current-category i {
        height: .9em;
    }
    .current-category {
        display: flex;
        align-items: center;
        background-color: var(--light-1);
        margin-bottom: 5px;
        border-radius: 10px;
        padding-left: 5px;
    }
    .current-category h4 {
        margin: 0px;
        padding-top: 10px;
        padding-bottom: 10px;
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
        padding-left: 5px;
        border-radius: 10px;
    }
    .category-item {
        display: flex;
        margin-bottom: 5px;
        padding-left: 5px;
        background-color: var(--light-1);
        border-radius: 10px;
        align-items: center;
    }
    .category-item i {
        height: .9em;
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
    .step-input {
        padding: 0px;
        margin: 10px;
        flex-grow: 1;
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
    .ingredient-row {
        display: flex;
        flex-direction: row;
        flex-wrap: nowrap;
        align-items: center;
        justify-content: start;
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
    .edit-block {
        background-color: var(--light-1);
        padding: 10px;
        border-radius: 10px;
        margin-top: 10px;
    }
</style>