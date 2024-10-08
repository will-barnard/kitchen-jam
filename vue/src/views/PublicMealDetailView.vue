<template>
    <div>
        <!-- TODO make header -->
        <p v-if="loading">Loading...</p>
        <PublicMealDetail v-if="!loading" :meal="getMeal"/>
    </div>
</template>

<script>
import PublicMealDetail from '../components/PublicMealDetail.vue';

export default {
    components: {PublicMealDetail},
    data() {
        return {
            getMeal: {},
            loading: true
        }
    },
    created() {
        if (this.$store.state.publicMealGallery.find((meal) => {return meal.mealId == this.$route.params.mealId;})) {
            this.getMeal = this.$store.state.publicMealGallery.find(
                (meal) => {
                    return meal.mealId == this.$route.params.mealId;
                }
            );
            this.loading = false;
        } else {
            this.$router.back();
        }
    }
}
</script>

<style scoped>
    
</style>