<template>
    <div class="display">
        <div>
            <div class="meal-img" v-if="img">
                <div>
                    <img :src="img" />
                </div>
            </div>
            <div class="single-row" v-show="showUser">
                    <div class="spacer"></div>
                    <div class="single-row user-field">
                        <p>cooked by </p>
                        <p class="username">{{ meal.userName }}</p>
                    </div>
                </div>
            <div class="details">
                <div class="title">
                    <h2 >{{ meal.mealName }}</h2>
                    <div class="single-row">
                        <h3 class="recipe" v-if="meal.recipeId" @click="showGoToRecipe = !showGoToRecipe">
                        <i class="fas fa-book"></i>&nbsp;{{ meal.recipeName }}
                        </h3>
                        <div v-if="showGoToRecipe" class="recipe-button-spacer">
                            <h3><i class="fas fa-share" @click="goToRecipe"></i></h3>
                        </div>
                    </div>
                    <div class="subtitle">
                        <h4 class="comment" v-show="meal.mealComment">{{ meal.mealComment }}</h4>
                        <h4 class="date" v-show="meal.dateCooked">{{ formatDate(meal.dateCooked) }}</h4>
                    </div>
                </div>

                <div class="tags" v-show="meal.tags.length > 0">
                    <p v-if="!meal.tags">No tags yet</p>
                    <div class="tag-list">
                        <div  v-for="tag in meal.tags">
                            <div class="tag-item-2">
                                <p>{{tag.tagName}}</p>
                            </div>    
                        </div>
                    </div>
                </div>

                <div class="widgets">
                    <div class="cooktime" v-show="meal.cookTime">
                        <img src="/img/clock.png" />
                        <p>{{ meal.cookTime }} min</p>
                    </div>
                    <div class="rating" v-show="meal.rating">
                        <p>{{ meal.rating }} / 10 Rating</p>
                    </div>
                </div>

                <div class="foot" v-show="meal.ingredients || meal.notes">
                    <div class="ingredients" v-show="meal.ingredients">
                        <h3>Ingredients:</h3>
                        <p>{{ meal.ingredients }}</p>
                    </div>
                    <div class="notes" v-show="meal.notes">
                        <h3 class="right" >Notes</h3>
                        <p>{{ meal.notes }}</p>
                    </div>
        
                </div>
                <div class="steplist" v-show="meal.ingredientList.length > 0">
                    <IngredientList :ingredientList="meal.ingredientList"/>
                </div>

            </div>            
        </div>
    </div>
</template>
<script>
import UtilityService from '../services/UtilityService.js';
import IngredientList from './IngredientList.vue';


export default {
    components: {IngredientList},
    props: ['meal', 'editable', 'img', 'showUser'],
    data() {
        return {
            showGoToRecipe: false
        }
    },
    methods: {
        formatDate(date) {
            return UtilityService.formatDate(date);
        },
        goToRecipe() {
            this.$router.push({name: 'recipe-detail', params: {recipeId: this.meal.recipeId}});
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
        flex-grow: 1;
        align-items: center;
        justify-content: center;
        padding: 5px;
        margin-top: 5px;
    }
    .recipe-button-spacer {
        margin-left: 5px;
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
        display: flex;
        align-items: center;
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
    .right {
        text-align: right;
    }
    .username {
        background-color: var(--light-8);
        padding: 5px;
        margin: 5px;
        border-radius: 5px;;
    }
    .user-field {
        background-color: var(--light-1);
        border-radius: 10px;
        padding-right: 10px;
        padding-left: 10px;
        margin-right: 0px;
        margin-left: 5px;
        margin-bottom: 5px;
    }
</style>