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


                    <div id="category" v-if="!recipe.category">
                        <h3>Category</h3>
                    </div> 


                    <div class="widget">
                        <p>{{ recipe.avgCookTime }} min</p>
                    </div>

                    <div class="info">
                        <p >{{ recipe.description }}</p>
                    </div>
                </div>
                
            </div>

            <Transition>
              <div id="controls" v-if="showMore">
                <div class="control" v-on:click="goDetail()"><img src="/img/detail.png" /></div>
            </div>  
            </Transition>
            
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
    .recipe-img {
        width: 40%;
    }
    .recipe-img img {
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
    .info {
        border-radius: 10px;
        flex-grow: 1;
    }
    .info p {
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
        margin: 0px;
    }
    .info h3 {
        text-align: left;
    }
    #notes {
        flex-grow: 1;
    }
    #tags {
        display: flex;
        flex-direction: row;
        justify-content: start;
        flex-wrap: wrap;
    }
    .subheader {
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }
    .widget {
        flex-grow: 1;
    }
    #controls {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    .control {
        width: 50px;
        padding: 10px;
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
    .v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
  transition: opacity 0s ease;

}
</style>