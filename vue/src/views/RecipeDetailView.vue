<template>
    <div>
        <RecipeMenu />
        <LoadingWidget v-if="loading" />
        <Transition>
            <RecipeDetail v-if="!loading" :recipe="recipe"/>
        </Transition>
    </div>
</template>

<script>
import RecipeMenu from '../components/RecipeMenu.vue';
import RecipeDetail from '../components/RecipeDetail.vue';
import LoadingWidget from '../components/LoadingWidget.vue';

export default {
    components: {RecipeMenu, RecipeDetail, LoadingWidget},
    data() {
        return {
            recipe: {},
            loading: true
        }
    },
    created() {
        if (this.$store.state.loadedRecipes) {
            this.recipe = this.$store.state.userRecipes.find(
                (recipeObj) => {
                    return recipeObj.recipeId == this.$route.params.recipeId;
                }
            );
            this.loading = false;
        } else {
            this.loadingTick();
        }
        
    },
    methods: {
        loadingTick() {
            setTimeout(() => {
                if (this.$store.state.loadedRecipes) {
                    this.recipe = this.$store.state.userRecipes.find(
                        (recipeObj) => {
                            return recipeObj.recipeId == this.$route.params.recipeId;
                        }
                    );
                    if (!this.recipe.imageId) {
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
                if (this.recipe.img) {
                    this.loading = false;
                } else {
                        this.imgTick();
                }
            }, 500);
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