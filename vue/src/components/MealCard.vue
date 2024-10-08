<template>
    <Transition>
    <div @click="showMore = !showMore" v-show="showImg = true">
        <body>
            <div id="details" class="body-card">
                <div class="meal-img" >
                    <img src="../img/placeholder.jpeg" v-if="!meal.imageId">
                    <img :src="meal.img" v-if="meal.imageId"/>
                </div>

                <div class="content">

                    <div class="title">
                        <h2 class="name">{{ meal.mealName }}</h2>
                    </div>
                    <div class="recipe" v-if="meal.recipeId">
                        <h3>{{ meal.recipeName }}</h3>
                    </div>
                    <div class="subheader">
                        <p>{{ meal.mealComment }}</p>
                        <p class="date">{{ formatDate(meal.dateCooked) }}</p>
                    </div>
                    

                    <div class="tags" v-if="meal.tags.length">
                        <div class="tag-list">
                            <div  v-for="tag in meal.tags">
                                <div class="tag-item">
                                    <p>{{tag.tagName}}</p>
                                </div>    
                            </div>
                    </div>
                    </div>
                    
                    <Transition name="control">
                        <div class="info" v-show="showMore">
                        <div class="row">
                        <div class="widget">
                            <p>{{ meal.cookTime }} min</p>
                        </div>
                        <div class="widget">
                            <p>{{ meal.rating }} / 10 Rating</p>
                        </div>
                    </div>
                        <h3>Ingredients:</h3>
                        <p >{{ meal.ingredients }}</p>
                        <h3>Notes:</h3>
                        <p id="notes">{{ meal.notes }}</p>
                    </div>
                    </Transition>
                </div>
                
            </div>

            <Transition name="control">
                <div>
                    <div id="controls" v-if="showMore && !isPublic">
                        <div class="control" v-on:click="goDetail()"><img src="/img/detail.png" /></div>
                    </div>
                    <div id="controls" v-if="showMore && isPublic">
                        <div class="control" v-on:click="goPublicDetail()"><img src="/img/detail.png" /></div>
                    </div>
                </div>
            </Transition>
            
        </body>
    </div>
    </Transition>
</template>
<script>
import UtilityService from '../services/UtilityService.js';
import ImageService from '../services/ImageService.js';
import Tag from './Tag.vue';

export default {
    components: {Tag},
    props: ['meal', 'isPublic'],
    data() {
        return {
            showMore: false        }
    },
    methods: {
        goDetail() {
            this.$router.push( {name: 'meal-detail', params: {mealId: this.meal.mealId} })
        },
        goPublicDetail() {
            this.$router.push( {name: 'public-meal-detail', params: {mealId: this.meal.mealId} })
        },
        switch() {
            this.showMore = !this.showMore;
        },
        formatDate(date) {
            return UtilityService.formatDate(date);
        }
    }
}
</script>

<style scoped>
    body {
        margin: 0px;
    }
    .body-card:hover {
        cursor: pointer;
    }
    .body-card p:hover {
        cursor: pointer;
    }
    #controls {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    .control {
        width: 50px;
        margin: 10px;
        padding: 5px;
        border-radius: 10px;
        font-size: 3em;
        margin-bottom: 10px;

    }
    .control:hover {
        cursor: pointer;
    }
    .control img {
        height: 1em;
    }
    .control {
        background-color: var(--edit);
    }
    #spacer {
        flex-grow: 1;
    }
    #details {
        display: flex;
        flex-direction: row;
        padding: 10px;
        border-radius: 10px;
        background-color: var(--light-2);
        margin-bottom: 10px;

    }
    p {
        text-align: left;
    }
    div {
        display: flex;
        flex-direction: column; 
    }
    h2, h3 {
        display: inline;
        text-align: center;
        margin: 5px;
    }
    p:hover {
        cursor: default;
    }
    .content {
        display: flex;
        flex-direction: column;    
        width: 50%;    
    }
    .row {
        flex-direction: row;
    }
    .meal-img {
        width: 40%;
    }
    .meal-img img {
        object-fit: cover;
        height: 100%;
        border-radius: 10px;
    }
    .content {
        flex-grow: 1;
        margin-left: 10px;
    }
    .title {
        text-align: center;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: end;
        border-radius: 10px;
        background-color: var(--light-1);

    }
    .recipe {
        text-align: center;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: end;
        border-radius: 10px;
        margin-top: 5px;
        background-color: var(--light-5);
    }
    .info {
        border-radius: 10px;
        flex-grow: 1;
    }
    .info p {
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin: 3px;
    }
    .info h3 {
        text-align: left;
    }
    #notes {
        flex-grow: 1;
    }
    .widget {
        flex-grow: 1;
    }
    .tags {
        margin-bottom: 5px;
    }
    .tag-list {
        display: flex;
        flex-direction: row;
        justify-content: start;
        flex-wrap: wrap;
        background-color: var(--light-1);
        padding: 5px;
        border-radius: 10px;
    }
    .tag-item {
        padding: 5px;
        background-color: var(--light-2);
        border-radius: 10px;
        margin-right: 5px;
        margin-bottom: 5px;
    }
    .tag-item p {
        margin: 0px;
    }
    .subheader {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }
    .date {
        text-align: right;
    }

    .v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
  transition: opacity 0.5s ease;
}
.control-enter-active,
.control-leave-active {
  transition: opacity 0.5s ease;
}

.control-enter-from,
.control-leave-to {
  opacity: 0;
  transition: opacity 0s ease;
}
</style>