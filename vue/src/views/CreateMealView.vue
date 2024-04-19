<template>
    <div>
        <MealMenu />
        <CreateMeal />
    </div>
</template>
<script>
import MealMenu from '../components/MealMenu.vue';
import CreateMeal from '../components/CreateMeal.vue';

export default {
    components: {MealMenu, CreateMeal}
    
}
</script>

<style scoped>
</style>