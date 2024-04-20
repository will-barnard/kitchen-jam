<template>
    <body>

       <div class="controls">
           <h2 @click="$router.go(-1)">Back</h2>
           <div class="spacer"></div>
           <h2 v-if="!editing" v-on:click="editing=true">
               Edit
           </h2>
           <h2 v-if="!editing" v-on:click="deleteButton()">
               Delete Recipe
           </h2>
           <h2 v-if="editing" v-on:click="saveEdit()">
               Save
           </h2>
           <h2 v-if="editing" v-on:click="cancelEdit()">
               Cancel
           </h2>
       </div>

       <div class="delete" v-if="deleting">
           <h2 id="delete-check">Are you sure you want to delete? This cannot be undone</h2>
           <h2 v-on:click="deleteRecipe()">Delete</h2>
           <h2 v-on:click="deleting = false">Cancel</h2>
       </div>

       <div v-show="!editing">
           <div>
               <div class="recipe-img">
               <img src="../img/placeholder.jpeg" />
               <!-- img goes here -->
               </div>
               <div class="details">
                   <div>
                       <h2>{{ staticRecipe.recipeName }}</h2>
                       <p>{{ staticRecipe.description }}</p>   
                   </div>

                   <!-- <h3>Last made here?</h3> -->

                   <!-- <div class="category">
                   </div> -->

                   <!-- <div>
                       <div class="widget">
                           <p>{{ staticRecipe.avgCookTime }} min</p>
                       </div>
                       <div class="widget">
                           <p>{{ staticRecipe.rating }} / 5 Rating</p>
                       </div>
                   </div> -->

                   
               </div>            
           </div>
       </div>

       <div v-show="editing">
           <form>
               <div class="recipe-img">
                   <img src="../img/placeholder.jpeg" />
                   <!-- img goes here -->
               </div>
           
               <div class="edit-form">
                   <div>
                       <label>Name</label><input type="text" v-model="newRecipe.recipeName">
                   </div>
                   <div>
                       <label>Description</label><input type="text" v-model="newRecipe.description"/>
                   </div>
                   <!-- <div class="edit-tags">
                       <div>
                           <label>Tags</label><input type="text" v-model="newTag.tagName"/>
                           <button>Add</button>
                       </div>
                       <div class="tag-list">
                           <p v-if="!newRecipe.tags">no tags</p>
                           <Tag class="tag" v-for="tag in staticRecipe.tags" :key="tag.tagId" :tag="tag" />
                       </div>

                   </div> -->
               </div>
           </form>
       </div>        
   </body>
</template>

<script>
import RecipeService from '../services/RecipeService.js'
import Tag from './Tag.vue';

export default {
    props: ['recipe', 'loading'],
    components: {Tag},
    data() {
        return {
            editing: false,
            newRecipe: {},
            staticRecipe: {},
            deleting: false,
            newTag: {}
        }
    },
    created() {
        this.staticRecipe = this.cloneRecipe(this.recipe);
        this.newRecipe = this.cloneRecipe(this.staticRecipe);
        console.log(this.recipe.tags);
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
                (response) => {
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

                newRecipe.categories = recipe.categories;

            }
            return newRecipe;
        }
    }
}
</script>

<style scoped>
    .recipe-img {
        height: 20vh;
        overflow: hidden;
        object-fit: cover;
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
    .edit-form {
        display: flex;
        flex-direction: column;
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
    .delete:hover {
        cursor: pointer;
    }
    #delete-check:hover {
        cursor: default;
    }
    .spacer {
        flex-grow: 1;
    }
    .tags {
        display: flex;
        flex-direction: row;
    }
    .edit-tags {
        display: flex;
        flex-direction: column;
    }
    .tag {
        border: 1px solid black;
        padding: 3px;
        padding-left: 15px;
        padding-right: 15px;
        border-radius: 50px;
    }
    .tag-list {
        display: flex;
        flex-direction: row;
    }

</style>