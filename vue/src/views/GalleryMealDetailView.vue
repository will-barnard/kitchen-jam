<template>
    <div>
        <!-- TODO make header -->
        <p v-if="loading">Loading...</p>
        <GalleryMealDetail v-if="!loading" :meal="getMeal"/>
    </div>
</template>

<script>
import GalleryMealDetail from '../components/GalleryMealDetail.vue';

export default {
    components: {GalleryMealDetail},
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