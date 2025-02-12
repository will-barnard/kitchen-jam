<template>
    <div>
        <TopBanner @click="$router.push({name: 'home'})"/>
        <LoadingWidget v-if="!show" />
        <Transition name="quickFade">
            <RecipeDetailCard :recipe="recipe" :editable="false" :img="img" :showUser="true" v-if="show" />
        </Transition>
    </div>
</template>

<script>
import TopBanner from '../components/TopBanner.vue';
import LoadingWidget from '../components/LoadingWidget.vue';
import ImageService from '../services/ImageService';
import RecipeService from '../services/RecipeService';
import RecipeDetailCard from '../components/RecipeDetailCard.vue';

export default {
    components: {TopBanner, LoadingWidget, RecipeDetailCard},
    data() {
        return {
            recipe: {},
            img: {},
            show: false,
            unauthorized: false
        }
    },
    created() {
        RecipeService.getPublicRecipe(this.$route.params.uuid).then(
            (response) => {
                this.recipe = response.data;
                if (!this.recipe.ingredientList) {
                    this.recipe.ingredientList = [];
                }
                if (!this.recipe.stepList) {
                    this.recipe.stepList = [];
                }
                console.log(this.recipe)
                if (this.recipe.imageId) {
                    ImageService.getImage(this.recipe.imageId).then(
                        (res) => {
                            const base64 = ImageService.parseImg(res);
                            this.img = "data:image/png;base64," + base64;
                            this.show = true;
                        }
                    )
                } else {
                    this.img = "/img/placeholder.jpeg";
                    this.show = true;
                }
            }
        )
    }
}
</script>

<style>
    
</style>