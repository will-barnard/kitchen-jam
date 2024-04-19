<template>
    <div>
        <MealMenu />
        <body>
            <MealLog />
        </body>
    </div>
</template>

<script>
import MealService from '../services/MealService.js';
import MealLog from '../components/MealLog.vue';
import MealMenu from '../components/MealMenu.vue';


export default {
    components: {MealLog, MealMenu},
    data() {
        return {
            mealList: [],
            tab: ""
        }
    },
    methods: {
        log() {
            this.tab = 'log';
        },
        newMeal() {
            this.tab = 'new';
        }
    },
    created() {
        this.tab = 'log';
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
    p:hover {
        cursor: pointer;
    }
</style>