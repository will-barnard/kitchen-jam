<template>
    <div @click="showMore = !showMore">
        <body>

            <div id="details" class="body-card">
                
                <div class="recipe-img" >
                    <img src="src/img/placeholder.jpeg" />
                    <!-- img goes here -->
                </div>

                <div class="content">

                    <div class="title">
                        <h2 class="name">{{ recipe.recipeName }}</h2>
                    </div>

<!-- 
                    <div id="tags" v-if="meal.tags">
                        <Tag v-for="tag in meal.tags" :key="tag.tagId" :tag="tag" edit="false"/>
                    </div> 
-->

                    <div class="widget">
                        <p>{{ recipe.avgCookTime }} min</p>
                    </div>

                    <div class="info">
                        <p >{{ recipe.description }}</p>
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

export default {
    props: ['recipe'],
    data() {
        return {
            showMore: false
        }
    },
    methods: {
        goDetail() {
            console.log("test");
            this.$router.push( {name: 'recipe-detail', params: {recipeId: this.recipe.recipeId} })
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
        font-size: 3em;
    }
    .control:hover {
        cursor: pointer;
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
    .recipe-img {
        width: 40%;
        border: 1px solid black;
    }
    .recipe-img img {
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
    #tags {
        display: flex;
        flex-direction: row;
        justify-content: start;
    }
</style>