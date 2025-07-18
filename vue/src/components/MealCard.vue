<template>
    <Transition>
    <div @click="showMore = !showMore" v-show="showImg = true" ref="observerTarget">
        <body>
            <div id="details" class="body-card">
                <div class="meal-img" >
                    <img src="../img/placeholder.jpeg" v-if="!meal.imageId">
                    <img :src="meal.img ? meal.img : '../img/placeholder.jpeg'" v-if="meal.imageId"/>
                </div>

                <div class="content">

                    <div class="title">
                        <h2 class="name">{{ meal.mealName }}</h2>
                    </div>
                    <div class="recipe" v-if="meal.recipeId">
                        <h3><i class="fas fa-book"></i> {{ meal.recipeName }}</h3>
                    </div>
                    <div v-if="isFeed" class="user-box">
                        <p class="name"><i class="fas fa-user"></i> {{ meal.userName }}</p>
                    </div>
                    <div class="date-box" v-if="meal.dateCooked">
                        <p class="date"><i class="fas fa-calendar-alt"></i> {{ formatDate(meal.dateCooked) }}</p>
                    </div>
                    <div class="subheader" v-if="meal.mealComment">
                        <p>{{ meal.mealComment }}</p>
                    </div>

                    <div class="tags" v-if="meal.tags.length > 0 && showMore">
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
                            <div class="widget cooktime" v-if="meal.cookTime > 0">
                                <p>Cooktime:&nbsp;{{ meal.cookTime }} min</p>
                            </div>
                            <div class="row">
                                <div class="widget" v-if="meal.rating > 0">
                                    <div class="stars">
                                        <div class="star-container">
                                            <span v-for="n in 5" :key="n" class="star">
                                                <i v-if="meal.rating >= n * 2" class="fas fa-star"></i>
                                                <i v-else-if="meal.rating >= (n * 2) - 1" class="fas fa-star-half-alt"></i>
                                                <i v-else class="far fa-star"></i>
                                            </span>
                                        </div>
                                        <p class="rating-label">Rating</p>
                                    </div>
                                </div>
                            </div>
                            <div v-if="meal.ingredients">
                                <h3>Ingredients:</h3>
                                <p >{{ meal.ingredients }}</p>
                            </div>
                            <div v-if="meal.notes">
                                <h3>Notes:</h3>
                                <p id="notes">{{ meal.notes }}</p>
                            </div>
                        </div>

                    </Transition>
                </div>
                
            </div>

            <Transition name="control">
                <div>
                    <div id="controls" v-if="showMore && !isPublic && !isFeed">
                        <div class="control" v-on:click="goDetail()">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </div>
                    </div>
                    <div id="controls" v-if="showMore && isPublic">
                        <div class="control" v-on:click="goPublicDetail()">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </div>
                    </div>
                    <div id="controls" v-if="showMore && isFeed">
                        <div class="control" v-on:click="goPublicDetail()">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </div>
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
    props: ['meal', 'isPublic', 'isFeed'],
    data() {
        return {
            showMore: false
        }
    },
    methods: {
        async loadImage() {
            if (this.meal.imageId && !this.meal.img) {
                const response = await ImageService.getImage(this.meal.imageId);
                const base64 = ImageService.parseImg(response);
                if (this.isFeed) {
                    this.meal.img = "data:image/png;base64," + base64;
                } else {
                    this.$store.commit('SAVE_IMAGE', { id: this.meal.imageId, base64, type: 'meal' });
                }
            }
        },
        goDetail() {
            this.$router.push( {name: 'meal-detail', params: {mealId: this.meal.mealId} })
        },
        goPublicDetail() {
            this.$router.push( {name: 'public-meal', params: {uuid: this.meal.publicUrl} })
        },
        switch() {
            this.showMore = !this.showMore;
        },
        formatDate(date) {
            return UtilityService.formatDate(date);
        },
        observeVisibility() {
            const observer = new IntersectionObserver((entries) => {
                if (entries[0].isIntersecting) {
                    this.loadImage();
                    observer.disconnect();
                }
            });
            observer.observe(this.$refs.observerTarget);
        }
    },
    mounted() {
        this.observeVisibility();
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
    .control i {
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
        background-color: var(--light-8);

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
        margin-top: 5px;
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
        padding: 5px;
        background-color: var(--light-2);
        border-radius: 10px;
        margin: 0px;
        margin-top: 5px;
    }
    .subheader p {
        margin: 0px;
    }
    .date {
        text-align: center;
    }
    .date-box p {
        padding: 5px;
        background-color: var(--light-1);
        border-radius: 10px;
        margin: 0px;
        margin-top: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .date-box p i {
        margin-right: 5px;
    }
    .user-box p {
        padding: 5px;
        background-color: var(--light-1);
        border-radius: 10px;
        margin: 0px;
        margin-top: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
    }
    .user-box p i {
        margin-right: 5px;
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
.stars {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: var(--light-1);
    border-radius: 10px;
    padding: 5px;
}
.star-container {
    display: flex;
    flex-direction: row;
}
.star {
    color: var(--light-5);
    margin: 0 2px;
}
.rating-label {
    text-align: center;
    margin-top: 5px;
    font-weight: bold;
}
.cooktime {
    background-color: var(--light-1);
    border-radius: 10px;
    padding: 5px;
    margin: 3px 0;
}
</style>