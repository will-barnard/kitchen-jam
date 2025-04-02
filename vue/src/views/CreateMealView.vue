<template>
    <div>
        <TopBanner/>
        <Transition name="quickFade">
            <div v-show="show">
                <CreateMeal />
            </div>
        </Transition>
    </div>
</template>
<script>
import MealMenu from '../components/MealMenu.vue';
import CreateMeal from '../components/CreateMeal.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {MealMenu, CreateMeal, TopBanner},
    data() {
        return {
            show: false
        }
    },
    created() {
        setTimeout(
        () => {this.show = true}
        , 10)
    }   
}
</script>

<style scoped>
</style>