<template>

    <body>
        <h1>Meal Log</h1>



        <nav>
            <p v-on:click="nav()">My Log</p>
            <p v-on:click="nav()">Log New Meal</p>
        </nav>

        <div>
            <p v-if="!mealList">you have not logged any meals</p>
            <MealCard class="meal-card" v-for="meal in $store.state.userMeals" :key="meal.mealId" :meal="meal"/>
        </div>
    </body>

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
    },
    methods: {
        nav() {
            this.$router.push({})
        }
    }
}
</script>

<style scoped>
    body {
        text-align: center;
    }
    nav {
        display: flex;
        flex-direction: row;
        justify-content: center;
    }
    div {
        margin: 0px;
    }
    p {
        margin: 0px;
        padding: 5px;
        display: inline;
        border: 1px solid black;
    }
</style>