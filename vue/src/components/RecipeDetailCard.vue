<template>
    <div>
        <div class="recipe-img" v-if="img">
            <div>
                <img :src="img" />
            </div>
        </div>
        <div class="details">
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
                            <p>{{ recipe.avgCookTime }} min</p>
                        </div>
                        <div v-show="recipe.avgCookTime == 0">
                            <p>no cooktime data available</p>
                        </div>
                    </div>
                </div>
                <div class="rating">
                    <!-- <div>
                        <p>rating / 10 Rating</p>
                    </div> -->
                    <div>
                        <p>no aggregate rating available</p>
                    </div>
                </div>
            </div>
        </div>            
    </div>
</template>

<script>
import IngredientList from './IngredientList.vue';
import StepList from './StepList.vue';

export default {
    components: {StepList, IngredientList},
    props: ['recipe', 'editable', 'img']
}
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
</style>