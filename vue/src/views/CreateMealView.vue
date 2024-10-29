<template>
    <div>
        <MealMenu />
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

export default {
    components: {MealMenu, CreateMeal},
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