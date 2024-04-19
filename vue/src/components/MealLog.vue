<template>
    <div>
        <p v-if="!mealList">you have not logged any meals</p>  
        <MealCard class="meal-card" v-for="meal in $store.state.userMeals" :key="meal.mealId" :meal="meal"/>
    </div>
</template>

<script>
import MealService from '../services/MealService.js';
import MealCard from '../components/MealCard.vue';

export default {
    components: {MealCard},
    data() {
        return {
            mealList: [],
            tab: ""
        }
    },
    created() {
        // this.tab = this.$route.params.tab
        if (!this.$store.state.userMeals) {
            this.mealList = this.$store.state.userMeals;
        } else {
            MealService.getMealsByUser().then(
                (response) => {
                    this.mealList = response.data;
                    this.$store.commit('GET_USER_MEALS', this.mealList);
                }
            )
        }
    }
}
</script>
<style>
    
</style>