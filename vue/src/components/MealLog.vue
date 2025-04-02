<template>
    <Transition>
    <div class="log">
        <div class="search-area">
            <input type="text" v-model="searchTerm" placeholder="Search meals..." class="search-input" />
        </div>
        <p v-if="!filteredMeals.length">you have not logged any meals</p>  
        <TransitionGroup>
            <MealCard class="meal-card" v-for="meal in filteredMeals" :key="meal.mealId" :meal="meal" :selected="select == meal.mealId" :isPublic="isPublic" v-on:click="selectCard()"/>
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
            select: 0,
            searchTerm: ''
        }
    },
    computed: {
        filteredMeals() {
            return this.mealList.filter(meal => 
                meal.mealName.toLowerCase().includes(this.searchTerm.toLowerCase())
            );
        }
    },
    methods: {
        selectCard() {

        }
    }
}
</script>
<style scoped>
    .log {
        margin-top: 0; 
        padding-top: 0;
    }
    .search-input {
        padding: 0.5rem;
        width: 100%;
        border-radius: 5px;
        border: 1px  var(--dark-1);
    }
    .search-area {
        display: flex;
        justify-content: center;
        margin-bottom: 1rem;
        padding: 1rem;
        background-color: var(--light-2);
        border-radius: 5px;
    }
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