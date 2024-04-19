<template>
    <div>
        <p v-if="!mealList">you have not logged any meals</p>  
        <MealCard class="meal-card" v-for="meal in $store.state.userMeals" :key="meal.mealId" :meal="meal" :selected="select == meal.mealId" v-on:click="selectCard()"/>
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
            select: 0
        }
    },
    created() {
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
    },
    methods: {
        selectCard() {

        }
    }
}
</script>
<style>
    
</style>