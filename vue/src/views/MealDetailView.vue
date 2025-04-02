<template>
    <div>
        <TopBanner />
        <LoadingWidget v-if="loading"/>
        <Transition>
            <MealDetail v-if="!loading" :meal="meal"/>
        </Transition>
    </div>
</template>

<script>
import MealMenu from '../components/MealMenu.vue';
import MealDetail from '../components/MealDetail.vue';
import LoadingWidget from '../components/LoadingWidget.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {MealMenu, MealDetail, LoadingWidget, TopBanner},
    data() {
        return {
            meal: {},
            loading: true
        }
    },
    created() {
        if (this.$store.state.loadedMeals) {
            this.meal = this.$store.state.userMeals.find(
                (mealObj) => {
                    return mealObj.mealId == this.$route.params.mealId;
                }
            )
            this.loading = false;
        } else {
            this.loadingTick();
        }
    },
    methods: {
        loadingTick() {
            setTimeout( () => {
                if (this.$store.state.loadedMeals) {
                    this.meal = this.$store.state.userMeals.find(
                        (mealObj) => {
                            return mealObj.mealId == this.$route.params.mealId;
                        }
                    );
                    this.loading = false;
                } else {
                    this.loadingTick();
                }
            }, 500)
        }
    }
}
</script>

<style scoped>
    .v-enter-active,
    .v-leave-active {
        transition: opacity .5s ease;
    }

    .v-enter-from,
    .v-leave-to {
        opacity: 0%;
    }
</style>