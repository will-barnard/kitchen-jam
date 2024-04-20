<template>
     <body>

        <div class="controls">
            <h2 @click="$router.go(-1)">Back</h2>
            <div class="spacer"></div>
            <h2 v-if="!editing" v-on:click="editing=true">
                Edit
            </h2>
            <h2 v-if="!editing" v-on:click="deleteButton()">
                Delete Meal
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
            <h2 v-on:click="deleteMeal()">Delete</h2>
            <h2 v-on:click="deleting = false">Cancel</h2>
        </div>

        <div v-show="!editing">
            <div>
                <div class="meal-img">
                <img src="../img/placeholder.jpeg" />
                <!-- img goes here -->
                </div>
                <div class="details">
                    <div>
                        <h2>{{ staticMeal.mealName }}</h2>
                        <h3>{{ staticMeal.date }}</h3>
                    </div>

                    <p>{{ staticMeal.mealComment }}</p>

                    <div class="tags">
                        <p v-if="!staticMeal.tags">no tags</p>
                        <Tag class="tag" v-for="tag in staticMeal.tags" :key="tag.tagId" :tag="tag" edit="false"/>
                    </div>

                    <div>
                        <div class="widget">
                            <p>{{ staticMeal.cookTime }} min</p>
                        </div>
                        <div class="widget">
                            <p>{{ staticMeal.rating }} / 5 Rating</p>
                        </div>
                    </div>

                    <div>
                        <p>{{ staticMeal.ingredients }}</p>
                        <p>{{ staticMeal.notes }}</p>
                    </div>
                </div>            
            </div>
        </div>

        <div v-show="editing">
            <form>
                <div class="meal-img">
                    <img src="../img/placeholder.jpeg" />
                    <!-- img goes here -->
                </div>
            
                <div class="edit-form">
                    <div>
                        <label>Name</label><input type="text" v-model="newMeal.mealName">
                    </div>
                    <div>
                        <label>Date cooked</label><input type="date" v-model="newMeal.date"/>
                    </div>
                    <div>
                        <label>Comment</label><input type="text" v-model="newMeal.mealComment"/>
                    </div>
                    <div class="edit-tags">
                        <div>
                            <label>Tags</label><input type="text" v-model="newTag.tagName"/>
                            <button>Add</button>
                        </div>
                        <div class="tag-list">
                            <p v-if="!newMeal.tags">no tags</p>
                            <Tag class="tag" v-for="tag in staticMeal.tags" :key="tag.tagId" :tag="tag" />
                        </div>

                    </div>
                    <div>
                        <label>Cook time (min)</label><input type="number" v-model="newMeal.cookTime"/>
                    </div>
                    <div>
                        <label>Rating</label><select v-model="newMeal.rating">
                        <option disabled="true" >----</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        </select>
                    </div>
                    <div>
                        <label>Ingredient list</label><input type="text" v-model="newMeal.ingredients"/>
                    </div>
                    <div>
                        <label>Notes</label><textarea v-model="newMeal.notes"></textarea>
                    </div>
                </div>
            </form>
        </div>        
    </body>
</template>

<script>
import MealService from '../services/MealService.js'
import Tag from './Tag.vue';

export default {
    props: ['meal', 'loading'],
    components: {Tag},
    data() {
        return {
            editing: false,
            newMeal: {},
            staticMeal: {},
            deleting: false,
            newTag: {}
        }
    },
    created() {
        this.staticMeal = this.cloneMeal(this.meal);
        this.newMeal = this.cloneMeal(this.staticMeal);
        console.log(this.meal.tags);
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
                (response) => {
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
                newMeal.mealName = meal.mealName;
                newMeal.mealComment = meal.mealComment;
                newMeal.date = meal.date;
                newMeal.cookTime = meal.cookTime;
                newMeal.notes = meal.notes;
                newMeal.ingredients = meal.ingredients;
                newMeal.rating = meal.rating;
                newMeal.imageId = meal.imageId;
                newMeal.tags = meal.tags;
            }
            return newMeal;
        }
    }
}
</script>

<style scoped>
    .meal-img {
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