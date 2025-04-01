<template>
    <Transition>
    <div @click="showMore = !showMore" ref="observerTarget">
        <body>

            <div id="details" class="body-card">
                
                <div class="recipe-img" >
                    <img src="../img/placeholder.jpeg" v-if="!recipe.imageId">
                    <img :src="recipe.img ? recipe.img : '../img/placeholder.jpeg'" v-if="recipe.imageId"/>
                </div>

                <div class="content">

                    <div class="title">
                        <h2 class="name">{{ recipe.recipeName }}</h2>
                    </div>


                    <div class="category" v-if="recipe.categoryId">
                        <h3>{{ recipe.categoryName }}</h3>
                    </div> 

                    <div v-if="isFeed" class="user-box">
                        <p class="name"><i class="fas fa-user"></i> {{ recipe.userName }}</p>
                    </div>

                    <div class="info" v-show="recipe.description">
                        <p >{{ recipe.description }}</p>
                    </div>

                    <div class="widget" v-show="recipe.avgCookTime > 0">
                        <p>Avg Cook TIme: {{ Math.floor(recipe.avgCookTime) }} min</p>
                    </div>
                    
                    <div class="rating" v-show="recipe.avgRating">
                        <div class="stars">
                            <p class="rating-label">Avg Rating</p>
                            <span v-for="n in 5" :key="n" class="star">
                                <i v-if="recipe.avgRating >= n * 2" class="fas fa-star"></i>
                                <i v-else-if="recipe.avgRating >= (n * 2) - 1" class="fas fa-star-half-alt"></i>
                                <i v-else class="far fa-star"></i>
                            </span>
                        </div>
                    </div>
                    
                    <div v-if="showMore && recipe.stepList">
                        <StepList :stepList="recipe.stepList" />
                    </div>
                    
                </div>
                
            </div>

            <Transition name="control">
              <div id="controls" v-if="showMore">
                <div class="control" v-on:click="goDetail()" v-if="!isFeed">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
                <div class="control" v-on:click="goPublicDetail()" v-if="isFeed">
                    <i class="fa-solid fa-magnifying-glass"></i>
                </div>
            </div>  
            </Transition>
            
        </body>
    </div>
    </Transition>
</template>

<script>
import StepList from './StepList.vue';
import ImageService from '../services/ImageService';

export default {
    props: ['recipe', 'isFeed'],
    components: {StepList},
    data() {
        return {
            showMore: false
        }
    },
    methods: {
        async loadImage() {
            if (this.recipe.imageId && !this.recipe.img) {
                const response = await ImageService.getImage(this.recipe.imageId);
                const base64 = ImageService.parseImg(response);
                if (this.isFeed) {
                    this.recipe.img = "data:image/png;base64," + base64;
                } else {
                    this.$store.commit('SAVE_IMAGE', { id: this.recipe.imageId, base64, type: 'recipe' });                }
            }
        },
        goDetail() {
            this.$router.push( {name: 'recipe-detail', params: {recipeId: this.recipe.recipeId} })
        },
        goPublicDetail() {
            this.$router.push( {name: 'public-recipe', params: {uuid: this.recipe.publicUrl} })
        },
        switch() {
            this.showMore = !this.showMore;
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
        width: 50%;
    }
    .title {
        text-align: center;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: end;
        border-radius: 10px;
        background-color: var(--light-5);
    }
    .info {
        border-radius: 10px;
        flex-grow: 1;
        margin-top: 5px;
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
        flex-grow: 0;
        background-color: var(--light-1);
        margin-top: 5px;
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 10px;
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    .widget p {
        margin: 0px;
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
    .control i {
        height: 1em;
    }
    .control {
        background-color: var(--edit);
    }
    .category {
        background-color: var(--light-6);
        margin-top: 5px;
        border-radius: 10px;
    }
    .stars {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        background-color: var(--light-1);
        border-radius: 10px;
        padding: 5px;
    }
    .stars p {
        margin: 0px;
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
  transition: opacity 0s ease;

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