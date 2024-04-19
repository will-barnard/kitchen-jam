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
        MealService.getMeal(this.$route.params.mealId).then(
            (response) => {
                this.getMeal = response.data;
                this.loading = false;
                console.log(response.data.tags);
            }
        )
    }
}
</script>

<style scoped>
    
</style>