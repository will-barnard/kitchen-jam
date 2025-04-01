<template>
    <div ref="observerTarget">
        <div class="recipe-img" v-if="localImg">
            <img :src="localImg" />
        </div>
        <div class="details">
            <div class="single-row" v-show="showUser">
                <div class="spacer"></div>
                <div class="single-row user-field">
                    <p>cooked by </p>
                    <p class="username">{{ recipe.userName }}</p>
                </div>
            </div>
            <div class="title">
                <h2 >{{ recipe.recipeName }}</h2>
            </div>
            <div class="category" v-show="recipe.categoryId">
                <h3>{{ recipe.categoryName }}</h3>
            </div>
            <div class="subtitle" v-show="recipe.description">
                <p>{{ recipe.description }}</p>   
            </div>
            <div class="steplist" v-show="recipe.ingredientList.length > 0">
                <IngredientList :ingredientList="recipe.ingredientList"/>
            </div>
            <div class="steplist" v-show="recipe.stepList.length > 0">
                <StepList :stepList="recipe.stepList"/>
            </div>
            <!-- <h3>Last made here?</h3> -->
            
            
            <div class="widgets">
                <div class="widgets">
                    <div class="widgets">
                        <div class="cooktime" v-show="recipe.avgCookTime > 0">
                            <img src="/img/clock.png" />
                            <p>{{ Math.floor(recipe.avgCookTime) }} min</p>
                        </div>
                        <div v-show="recipe.avgCookTime == 0">
                            <p>no cooktime data available</p>
                        </div>
                    </div>
                </div>
                <div class="rating" v-show="recipe.avgRating">
                    <div class="stars">
                        <span v-for="n in 5" :key="n" class="star">
                            <i v-if="recipe.avgRating >= n * 2" class="fas fa-star"></i>
                            <i v-else-if="recipe.avgRating >= (n * 2) - 1" class="fas fa-star-half-alt"></i>
                            <i v-else class="far fa-star"></i>
                        </span>
                    </div>
                    <p class="rating-label">Avg Rating</p>
                </div>
            </div>
        </div>            
    </div>
</template>

<script>
import IngredientList from './IngredientList.vue';
import StepList from './StepList.vue';
import ImageService from '../services/ImageService';

export default {
  components: { StepList, IngredientList },
  props: ['recipe', 'editable', 'img', 'showUser'],
  data() {
    return {
      localImg: "/img/placeholder.jpeg" // Initialize with placeholder
    };
  },
  methods: {
    async loadImage() {
      if (this.recipe.imageId) { // Remove the check for !this.localImg
        const response = await ImageService.getImage(this.recipe.imageId);
        const base64 = ImageService.parseImg(response);
        this.localImg = `data:image/png;base64,${base64}`; // Update with the loaded image
        this.$store.commit('SAVE_IMAGE', { id: this.recipe.imageId, base64, type: 'recipe' });
      }
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
};
</script>

<style scoped>
    body p, h1, h2, h3, h4 {
        margin: 0px;
    }
    .recipe-img {
        margin-top: 10px;
        overflow: hidden;
        text-align: center;
        margin-bottom: 15px;
        margin-left: 5px;
        margin-right: 5px;
    }
    .recipe-img img {
        width: 100%;
        border-radius: 15px;
    }
    .spacer {
        flex-grow: 1;
    }
    .button {
        width: 15vw;
        /* border: 1px solid var(--border-color); */
        border-radius: 10px;
        padding: 5px;
        margin-right: 5px;
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
        background-color: var(--light-5);
        border-radius: 10px;
        padding: 10px;
        margin: 0px;
        margin-bottom: 5px;
    }
    .title h3 {
        text-align: center;
        background-color: var(--light-5);
        border-radius: 10px;
        padding: 10px;
        margin: 0px;
        margin-top: 5px;

    }
    .title h4{
        margin-top: 5px;
        margin-bottom: 0px;
    }
    .subtitle {
        margin-left: 0px;
        padding: 10px;
        background-color: var(--light-1);
        border-radius: 10px;
        flex-grow: 1;
    }
    .widgets {
        display: flex;
        margin-top: 15px;
        justify-content: center;
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
    .stars {
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        background-color: var(--light-2);
        border-radius: 10px;
        padding: 5px;
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
    .meals h2 {
        margin: 10px;
    }
    .input-area {
        display: flex;
        flex-direction: column;
        margin-bottom: 5px;
    }
    .edit-form {
        display: flex;
        flex-direction: column;
        background-color: var(--light-2);
        margin: 5px;
        padding: 10px;
        border-radius: 10px;
    }
    .edit-form h3 {
        margin-bottom: 5px;
    }
    .category-search {
        display: flex;
        margin-bottom: 5px;
    }
    .category-search input {
        margin: 0px;
        flex-grow: 1;
    }
    .tag-spacer {
        width: 5px;
    }
    .category {
        text-align: center;
        background-color: var(--light-6);
        margin-bottom: 5px;
        padding: 5px;
        border-radius: 10px;
    }
   
    .edit-image h3 {
        margin-bottom: 5px;
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