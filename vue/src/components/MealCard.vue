<template>
    <div @click="showMore = !showMore">
        <body>

            <div id="details" class="body-card">
                <div class="meal-img" >
                    <img src="src/img/placeholder.jpeg" />
                    <!-- img goes here -->
                </div>

                <div class="content">

                    <div class="title">
                        <h2 class="name">{{ meal.mealName }}</h2>
                        <h3>{{ meal.date }}</h3>
                    </div>

                    <p>{{ meal.mealComment }}</p>


                    <div class="row">
                        <div class="widget">
                            <p>{{ meal.cookTime }} min</p>
                        </div>
                        <div class="widget">
                            <p>{{ meal.rating }} / 5 Rating</p>
                        </div>
                    </div>

                    <div class="info" v-show="showMore">
                        <div id="tags" v-if="meal.tags">
                            <Tag v-for="tag in meal.tags" :key="tag.tagId" :tag="tag" edit="false"/>
                        </div>
                        <p >{{ meal.ingredients }}</p>
                        <p id="notes">{{ meal.notes }}</p>
                    </div>
                </div>
                
            </div>


            <div id="controls" v-if="showMore">
                <div class="control" v-on:click="goDetail()">...</div>
            </div>
        </body>
    </div>
</template>
<script>
import Tag from './Tag.vue';

export default {
    components: {Tag},
    props: ['meal'],
    data() {
        return {
            showMore: false
        }
    },
    methods: {
        goDetail() {
            this.$router.push( {name: 'meal-detail', params: {mealId: this.meal.mealId} })
        },
        switch() {
            this.showMore = !this.showMore;
        }
    },
    created() {
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
        border: 1px solid var(--border-color);
        border-radius: 10px;
        font-size: 3em;
    }
    .control:hover {
        cursor: pointer;
    }
    #spacer {
        flex-grow: 1;
    }
    #details {
        display: flex;
        flex-direction: row;
        padding: 10px;
        border: 1px solid var(--border-color);
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
    }
    .row {
        flex-direction: row;
    }
    .meal-img {
        width: 40%;
        border: 1px solid var(--border-color);
    }
    .meal-img img {
        object-fit: cover;
        height: 100%;
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
        border: 1px solid var(--border-color);
        border-radius: 10px;
    }
    .info {
        border: 1px solid var(--border-color);
        border-radius: 10px;
        flex-grow: 1;
        padding: 5px;
    }
    .info p {
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin: 3px;
    }
    #notes {
        flex-grow: 1;
    }
    .widget {
        flex-grow: 1;
    }
    #tags {
        display: flex;
        flex-direction: row;
        justify-content: start;
        flex-wrap: wrap;
    }
</style>