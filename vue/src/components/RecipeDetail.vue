<template>
    <Transition>
    <body>
        <div class="recipe-img">
            <div>
                <img :src="imgPath" />
            </div>
        </div>

       <div v-show="!editing">
           <RecipeDetailCard :recipe="staticRecipe" :editing="true"/>
       </div>

       <div v-show="editing">
            <div class="edit-image edit-block">
                <h3>Edit image</h3>
                <input type="file" name="file" accept="image/*" @change="uploadImage">
            </div>
            <div class="edit-form">
                <Transition name="quickFade">
                    <div v-if="newRecipe.public">
                        <div class="single-row">
                            <h3>Sharing</h3>
                            <div class="spacer"></div>
                            <p>Public &#127760;</p>
                        </div>
                        <div class="single-row">
                            <button @click.prevent="unsecuredCopyToClipboard(newRecipe.publicUrl)">Copy URL</button>
                            <div class="spacer">
                                <Transition name="quickFade">
                                    <p v-if="copiedURL">Link copied!</p>
                                </Transition>
                            </div>
                            <button @click.prevent="makePrivate()">Make private</button>
                        </div>
                    </div>
                </Transition>
                <Transition name="quickFade">
                    <div v-if="!newRecipe.public">
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
            <div class="edit-form edit-category">
                <h3>Category</h3>
                <div>
                    <div v-if="newRecipe.categoryId" class="current-category">
                        <i class="fas fa-minus minus mini-button" @click="removeCategory()"></i>
                        <div class="tag-spacer"></div>
                        <h4>{{ newRecipe.categoryName }}</h4>
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
           <form>
               <div class="edit-form">
                    <h3>Details</h3>

                   <div class="input-area">
                       <label>Name</label>
                       <input type="text" v-model="newRecipe.recipeName">
                   </div>
                   <div class="input-area">
                       <label>Description</label>
                       <textarea v-model="newRecipe.description"></textarea>
                   </div>
                </div>
                <div class="edit-form">
                   <div class="input-area">
                        <h3>Ingredients</h3>
                        <div v-for="ingredient in newRecipe.ingredientList" :key="ingredient.listOrder" class="single-row">
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
                <div class="edit-form">
                   <div class="input-area">
                       <h3>Steps</h3>
                       <div v-for="step in newRecipe.stepList" :key="step.stepOrder" class="single-row">
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
               </div>
           </form>
       </div>



       <div class="delete" v-if="deleting">
           <p id="delete-check">Are you sure you want to delete? This cannot be undone</p>
           <h2 class="yes-delete" v-on:click="deleteRecipe()">Delete</h2>
           <h2 class="cancel-delete" v-on:click="deleting = false">Cancel</h2>
       </div>
       <div class="controls" v-show="!deleting">
            <div class="link-button button" v-show="staticRecipe.public" @click="unsecuredCopyToClipboard(newRecipe.publicUrl)">
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
        <div class="meals" v-if="mealList.length > 0 && !editing">
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
import StepList from './StepList.vue';
import RecipeDetailCard from './RecipeDetailCard.vue';

function cloneRecipe(recipe) {
    let newRecipe = {};
    if (recipe) {
        newRecipe.recipeId = recipe.recipeId;
        newRecipe.userId = recipe.userId;
        newRecipe.recipeName = recipe.recipeName;
        newRecipe.description = recipe.description;
        newRecipe.avgCookTime = recipe.avgCookTime;
        newRecipe.avgRating = recipe.avgRating;
        newRecipe.imageId = recipe.imageId;
        newRecipe.public = recipe.public;
        newRecipe.publicUrl = recipe.publicUrl;
        newRecipe.categoryId = recipe.categoryId;
        newRecipe.categoryName = recipe.categoryName;
        newRecipe.mealList = recipe.mealList;
        newRecipe.img = recipe.img;

        newRecipe.categories = recipe.categories;
        newRecipe.ingredientList = recipe.ingredientList;

        newRecipe.stepList = [];
        for(let i = 0; i < recipe.stepList.length; i++) {
            let step = Object.assign({}, recipe.stepList[i])
            newRecipe.stepList.push(step);
        }
    }
    return newRecipe;
}

export default {
    props: ['recipe', 'loading'],
    components: {Tag, MealCard, StepList, RecipeDetailCard},
    data() {
        return {
            editing: false,
            showImage: !!this.recipe.img,
            imgPath: this.recipe.img || "../img/placeholder.jpeg",
            newRecipe: {},
            staticRecipe: {},
            deleting: false,
            newTag: {},
            newCategory: {},
            searchCategory: [],
            mealList: [],
            newStep: "",
            newIngredient: "",
            newIngredientQuantity: "",
            copiedURL: false
        }
    },
    created() {
        this.mealList = this.$store.state.userMeals.filter(
            (meal) => {
                return meal.recipeId == this.recipe.recipeId;
            }
        )
        if (this.recipe.stepList == null) {
            this.recipe.stepList = [];
        }
        this.staticRecipe = cloneRecipe(this.recipe);
        this.newRecipe = cloneRecipe(this.staticRecipe);        

        // img handling
        if (this.recipe.imageId == 0 || this.recipe.imageId == null) {
            this.imgPath = "../img/placeholder.jpeg";
            this.showImage = true;
        } else {
            this.imgPath = this.recipe.img;
            this.showImage = true;
        }

    },
    methods: {

        // EDIT methods

        cancelEdit() {
            this.editing = false;
            this.newRecipe = cloneRecipe(this.staticRecipe);
        },
        saveEdit() {
            // todo implement this better?
            this.newRecipe.updateSteps = true;

            RecipeService.updateRecipe(this.newRecipe).then(
                (response) => {
                    let newRecipe = response.data;
                    if (newRecipe.imageId) {
                        ImageService.getImage(newRecipe.imageId).then(
                            (r) => {
                                const base64 = ImageService.parseImg(r);
                                newRecipe.img = "data:image/png;base64," + base64;
                            }
                        )
                    }
                    this.$store.commit('UPDATE_RECIPE', newRecipe)
                    this.staticRecipe = cloneRecipe(response.data);
                    this.newRecipe = cloneRecipe(this.staticRecipe);
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
            this.$store.commit('DELETE_RECIPE', this.recipe.recipeId)
            this.$router.push({name: 'cookbook'})
        },

        // CATEGORY methods

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
        },

        // STEP methods

        addStep() {
            let step = {};
            step.stepDescription = this.newStep;
            step.stepOrder = this.newRecipe.stepList.length + 1;
            this.newRecipe.stepList.push(step);
            this.newStep = "";
        },
        moveStep(k, step) {
            if (k == 1) {
                // move down the list
                if (step.stepOrder != this.newRecipe.stepList.length) {
                    let moveStep = Object.assign(step.stepDescription);
                    let pivotStep = this.newRecipe.stepList[step.stepOrder];
                    step.stepDescription = pivotStep.stepDescription;
                    pivotStep.stepDescription = moveStep;
                }
            }
            if (k == -1) {
                // move up the list
                if (step.stepOrder != 1) {
                    let moveStep = Object.assign(step.stepDescription);
                    let pivotStep = this.newRecipe.stepList[step.stepOrder - 2];
                    step.stepDescription = pivotStep.stepDescription;
                    pivotStep.stepDescription = moveStep;
                }
            }
        },
        deleteStep(step) {
            let list = this.newRecipe.stepList;
            if (step.stepOrder == list.length) {
                // case for last on list
                list.pop();
            }
            else if (step.stepOrder == 1) {
                // case for first on list
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
                // case for middle of list
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

        // INGREDIENT methods

        addIngredient() {
            let ingredient = {};
            ingredient.ingredientName = this.newIngredient;
            ingredient.listOrder = this.newRecipe.ingredientList.length + 1;
            ingredient.quantity = this.newIngredientQuantity;
            this.newRecipe.ingredientList.push(ingredient);
            this.newIngredient = "";
            this.newIngredientQuantity = "";
        },
        moveIngredient(k, ingredient) {
            if (k == 1) {
                // move down the list
                if (ingredient.listOrder != this.newRecipe.ingredientList.length) {
                    let moveIngredient = Object.assign(ingredient.ingredientName);
                    let moveQuantity = Object.assign(ingredient.quantity);
                    let pivotIngredient = this.newRecipe.ingredientList[ingredient.listOrder];
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
                    let pivotIngredient = this.newRecipe.ingredientList[ingredient.listOrder - 2];
                    ingredient.ingredientName = pivotIngredient.ingredientName;
                    ingredient.quantity = pivotIngredient.quantity;
                    pivotIngredient.ingredientName = moveIngredient;
                    pivotIngredient.quantity = moveQuantity;
                }
            }
        },
        deleteIngredient(ingredient) {
            let list = this.newRecipe.ingredientList;
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

        // PUBLIC methods
        // TODO implement store here?


        makePublic() {
            RecipeService.makePublic(this.recipe.recipeId).then(
                (response) => {
                    this.newRecipe.publicUrl = response.data;
                    this.newRecipe.public = true;
                    this.staticRecipe.publicUrl = response.data;
                    this.staticRecipe.public = true;

                }
            )
        },
        makePrivate() {
            RecipeService.makePrivate(this.recipe.recipeId).then(
                () => {
                    this.newRecipe.publicUrl = null;
                    this.newRecipe.public = false;
                    this.staticRecipe.publicUrl = null;
                    this.staticRecipe.public = false;
                }
            );
        },

        // TODO FIX IMG method

        uploadImage(event){
            this.showImage = false;
            
            if (this.recipe.imageId == 0 || this.recipe.imageId == null) {
                ImageService.createImage(event.target.files[0]).then(
                    (response) => {
                        this.newRecipe.imageId = response.data;
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
        async copyURL(mytext) {
            try {
                await navigator.clipboard.writeText(mytext);
                this.copiedURL = true;
                setTimeout(() => this.copiedURL = false, 2000);
            } catch($e) {
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
            },
        async loadImage() {
            if (this.recipe.imageId && !this.recipe.img) {
                const response = await ImageService.getImage(this.recipe.imageId);
                const base64 = ImageService.parseImg(response);
                this.imgPath = `data:image/png;base64,${base64}`;
                this.showImage = true;
                this.$store.commit('SAVE_IMAGE', { id: this.recipe.imageId, base64, type: 'recipe' });
            }
        }
    },
    mounted() {
        this.loadImage();
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
    .controls i {
        font-size: 2em; /* Increase the size of the icons */
        margin: 0 10px; /* Add some margin for spacing */
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
    .cooktime i {
        font-size: 2em; /* Increase the size of the icons */
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
        margin-bottom: 15px;
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
    .category-item i {
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
</style>