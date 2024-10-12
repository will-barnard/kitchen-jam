<template>
    <div>
        <h1>Will's Page</h1>
        <MealLog v-if="show" :mealList="mealList" :isPublic="true"/>
        <p v-if="!show">Loading...</p>
    </div>
</template>

<script>
import WillService from '../services/WillService';
import MealLog from '../components/MealLog.vue';

export default {
    components: {MealLog},
    data() {
        return {
            mealList: [],
            show: false
        }
    },
    created() {
        WillService.getMeals().then(
            (result) => {
                this.$store.commit('LOAD_MEAL_GALLERY', result.data)
                this.mealList = this.$store.state.publicMealGallery;
                this.show = true;
            }
        )
    }
}
</script>

<style>
    
</style>