<template>
    <div>
        <TopBanner @click="$router.push({name: 'home'})"/>
        <LoadingWidget v-if="!show"/>
        <Transition name="quickFade">
            <div v-if="show">
                <MealDetailCard :meal="meal" :editable="false" :img="img" :showUser="true" />
            </div>
        </Transition>
    </div>
</template>

<script>
import MealService from '../services/MealService';
import ImageService from '../services/ImageService';
import LoadingWidget from '../components/LoadingWidget.vue';
import MealDetailCard from '../components/MealDetailCard.vue';
import TopBanner from './TopBanner.vue';

export default {
    components: {LoadingWidget, MealDetailCard, TopBanner},
    data() {
        return {
            meal: {},
            img: {},
            show: false,
            unauthorized: false
        }
    },
    created() {
        MealService.getPublicMeal(this.$route.params.uuid).then(
            (response) => {
                this.meal = response.data;
                if (!this.meal.ingredientList) {
                    this.meal.ingredientList = [];
                }
                if (!this.meal.tags) {
                    this.meal.tags = [];
                }
                if (this.meal.imageId) {
                    ImageService.getImage(this.meal.imageId).then(
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