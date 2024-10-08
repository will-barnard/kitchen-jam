<template>
    <Transition>

    <div class="log">
        <p v-if="!mealList">you have not logged any meals</p>  
        <TransitionGroup>
            <MealCard class="meal-card" v-for="meal in mealList" :key="meal.mealId" :meal="meal" :selected="select == meal.mealId" :isPublic="isPublic" v-on:click="selectCard()"/>
        </TransitionGroup>
    </div>
</Transition>
</template>

<script>
import MealService from '../services/MealService.js';
import MealCard from '../components/MealCard.vue';

export default {
    components: {MealCard},
    props: ['mealList', 'isPublic'],
    data() {
        return {
            select: 0
        }
    },
    created() {
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