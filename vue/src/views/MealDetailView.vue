<template>
    <div>
        <MealMenu />
        <p v-if="loading">Loading...</p>
        <MealDetail v-if="!loading" :meal="getMeal"/>
    </div>
</template>

<script>
import MealService from '../services/MealService.js';
import MealMenu from '../components/MealMenu.vue';
import MealDetail from '../components/MealDetail.vue';

export default {
    components: {MealMenu, MealDetail},
    data() {
        return {
            getMeal: {},
            loading: true
        }
    },
    created() {
        if (this.$store.state.userMeals.find((meal) => {return meal.mealId = this.$route.params.mealId;})) {
            this.getMeal = this.$store.state.userMeals.find(
                (meal) => {
                    return meal.mealId = this.$route.params.mealId;
                }
            );
            this.loading = false;
        } else {
            MealService.getMeal(this.$route.params.mealId).then(
                (response) => {
                    this.getMeal = response.data;
                    this.loading = false;
                }
            )
        }
    }
}
</script>

<style scoped>
    
</style>