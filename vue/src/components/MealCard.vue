<template>
    <div>
    
        <body>

            <div id="details" v-on:click="showMore = true" :class="showMore == true ? 'body-detail' : 'body-card'">
                <div class="meal-img" v-on:click="showMore = !showMore">
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
                        <p >{{ meal.ingredients }}</p>
                        <p id="notes">{{ meal.notes }}</p>
                    </div>
                </div>
                
            </div>
            <div id="controls" v-if="showMore">
                <div class="control" v-on:click="showMore = false">^</div>
                <div id="spacer"></div>
                <div class="control" v-on:click="goDetail()">✎</div>
                <div class="control">X</div>
            </div>
        </body>

 
    </div>

</template>
<script>
export default {
    props: ['meal'],
    data() {
        return {
            showMore: false
        }
    },
    methods: {
        goDetail() {
            this.$router.push( {name: 'meal-detail', params: {mealId: this.meal.mealId} })
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
        padding: 10px;
        border: 1px solid black;
        border-radius: 100%;
        font-size: 3em;
    }
    .control:hover {
        cursor: pointer;
    }
    #spacer {
        flex-grow: 1;
    }
    footer {
        border: 1px solid black;
    }
    #details {
        display: flex;
        flex-direction: row;
        padding: 5px;
        border: 1px solid black;
        height: 25vh;
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
        border: 1px solid black;
    }
    .meal-img img {
        object-fit: cover;
        height: 25vh;
    }
    .content {
        flex-grow: 1;
        margin-left: 10px;
    }
    .title {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: end;
        border: 1px solid black;
    }
    .info {
        border: 1px solid black;
        flex-grow: 1;
    }
    #notes {
        flex-grow: 1;
    }
    .widget {
        flex-grow: 1;
    }
</style>