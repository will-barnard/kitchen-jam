<template>
    <div>
        <TopBanner />
        <Transition name="quickFade">
            <div v-show="show">
                <CreateRecipe />
            </div>
        </Transition>    
    </div>
</template>

<script>
import RecipeMenu from '../components/RecipeMenu.vue';
import CreateRecipe from '../components/CreateRecipe.vue';
import TopBanner from '../components/TopBanner.vue';

export default {
    components: {RecipeMenu, CreateRecipe, TopBanner},
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

<style>
    
</style>