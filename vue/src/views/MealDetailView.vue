<template>
    <div>
        <MealMenu />
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

export default {
    components: {MealMenu, MealDetail, LoadingWidget},
    data() {
        return {
            meal: {},
            loading: true
        }
    },
    created() {
        if (this.$store.state.loadedMeals) {
            let tempMeal = this.$store.state.userMeals.find(
                (mealObj) => {
                    return mealObj.mealId == this.$route.params.mealId;
                }
            )
            if (tempMeal) {
                this.meal = tempMeal;
                this.loading = false;
            } else {
                this.createdTick();
            }
            
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
                    if (!this.meal.imageId) {
                        this.loading = false;
                    } else {
                        this.imgTick();
                    }
                } else {
                    this.loadingTick();
                }
            }, 500)
        },
        imgTick() {
            setTimeout( () => {
                if (this.meal.img) {
                    this.loading = false;
                } else {
                        this.imgTick();
                }
            }, 500);
        },
        createdTick() {
            setTimeout( () => {
                let tempMeal = this.$store.state.userMeals.find(
                    (mealObj) => {
                        return mealObj.mealId == this.$route.params.mealId;
                    })
                if (tempMeal) {
                    this.meal = tempMeal;
                    this.loading = false;
            } else {
                this.createdTick();
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