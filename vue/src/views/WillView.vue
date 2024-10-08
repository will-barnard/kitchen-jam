<template>
    <div>
        <h1>Will's Page</h1>
        <body>
            <MealLog :mealList="mealList" :isPublic="true"/>
        </body> 
    </div>
</template>

<script>
import WillService from '../services/WillService';
import MealLog from '../components/MealLog.vue';

export default {
    components: {MealLog},
    data() {
        return {
            mealList: []
        }
    },
    created() {
        WillService.getMeals().then(
            (result) => {
                this.$store.commit('LOAD_MEAL_GALLERY', result.data)
                this.mealList = this.$store.state.publicMealGallery;
            }
        )
    }
}
</script>

<style>
    
</style>