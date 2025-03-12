<template>
    <div class="main">
        <div class="recipe-img" >
            <img src="../img/placeholder.jpeg" v-if="!recipe.imageId">
            <img :src="recipe.img ? recipe.img : '../img/placeholder.jpeg'" v-if="recipe.imageId"/>
        </div>
        <div>
            <h3>{{ recipe.recipeName }}</h3>
        </div>
        <h3><i class="fas fa-share" @click="goToRecipe"></i></h3>
    </div>
</template>


<script>
export default {
    props: ['recipe'],
    methods: {
        goToRecipe() {
            this.$router.push({ name: 'recipe-detail', params: { recipeId: this.recipe.recipeId } });
        }
    }
}
</script>

<style scoped>
    h3 {
        margin: 0px;
        text-align: center;
    }
    .main {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: var(--light-1);
        border-radius: 15px;
        padding: 10px;
        margin: 5px;
        margin-left: 10px;
        margin-right: 10px;
        width: 100%;
    }
    .recipe-img img {
        width: 100px; /* Adjust the width as needed */
        height: 75px; /* Ensure all images have the same height */
        object-fit: cover;
        border-radius: 15px;
    }
    .spacer {
        flex-grow: 1;
    }
    i {
        margin: 10px;
        border: 1px;
        border-radius: 10px;
        padding: 5px;
        background-color: var(--light-5);
    }
</style>