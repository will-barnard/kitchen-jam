<template>
    <Transition>

    <div class="log">
        <p v-if="!mealList">you have not logged any meals</p>  
        <TransitionGroup>
            <MealCard class="meal-card" v-for="meal in $store.state.userMeals" :key="meal.mealId" :meal="meal" :selected="select == meal.mealId" v-on:click="selectCard()"/>
        </TransitionGroup>
    </div>
</Transition>
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
<style scoped>
    .v-enter-active,
.v-leave-active {
  transition: opacity 0.5s ease;
}

.v-enter-from,
.v-leave-to {
  opacity: 0;
  transition: opacity 0.5s ease;
}
</style>